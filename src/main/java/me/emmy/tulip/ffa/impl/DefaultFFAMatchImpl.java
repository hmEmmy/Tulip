package me.emmy.tulip.ffa.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.cooldown.Cooldown;
import me.emmy.tulip.cooldown.CooldownRepository;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.hotbar.HotbarUtility;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Remi
 * @project Tulip
 * @date 5/27/2024
 */
public class DefaultFFAMatchImpl extends AbstractFFAMatch {

    /**
     * Constructor for the DefaultFFAMatchImpl class
     *
     * @param name       The name of the match
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    public DefaultFFAMatchImpl(String name, Arena arena, Kit kit, int maxPlayers) {
        super(name, arena, kit, maxPlayers);
    }

    /**
     * Join a player to the FFA match
     *
     * @param player The player
     */
    @Override
    public void join(Player player) {
        if (getPlayers().size() >= getMaxPlayers()) {
            player.sendMessage(CC.translate(ConfigHandler.getInstance().getLocaleConfig().getString("game.is-full").replace("{max-players}", String.valueOf(getMaxPlayers()))));
            return;
        }

        for (String welcomer : ConfigHandler.getInstance().getLocaleConfig().getStringList("game.join-message")) {
            player.sendMessage(CC.translate(welcomer)
                    .replace("{kit}", getKit().getName())
                    .replace("{arena}", getArena().getName())
            );
        }

        getPlayers().add(player);
        setupPlayer(player);
    }

    /**
     * Leave a player from the FFA match
     *
     * @param player The player
     */
    @Override
    public void leave(Player player) {
        getPlayers().remove(player);

        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);

        player.sendMessage(CC.translate(ConfigHandler.getInstance().getLocaleConfig().getString("game.left")));

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPAWN);
        profile.setFfaMatch(null);

        CooldownRepository cooldownRepository = Tulip.getInstance().getCooldownRepository();
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));
        if (optionalCooldown.isPresent()) {
            Cooldown cooldown = optionalCooldown.get();
            cooldown.cancelCooldown();
        }

        profile.getStats().resetStreakAndSaveIfPresent(getKit(), profile.getStats().getHighestKillstreak(getKit()), player.getUniqueId());

        PlayerUtil.reset(player);
        Tulip.getInstance().getSpawnHandler().teleportToSpawn(player);
        HotbarUtility.applyHotbarItems(player);
        player.getInventory().setHeldItemSlot(0);
    }

    /**
     * Setup a player for the FFA match
     *
     * @param player The player
     */
    @Override
    public void setupPlayer(Player player) {
        if (player.isFlying()) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setFfaMatch(this);

        Arena arena = getArena();
        player.getInventory().setHeldItemSlot(0);
        player.teleport(arena.getSpawn());

        Kit kit = getKit();
        player.getInventory().setArmorContents(kit.getArmor());
        player.getInventory().setContents(profile.getKitLayout().getLayout(kit.getName()) == null ? kit.getItems() : profile.getKitLayout().getLayout(kit.getName()));
    }

    /**
     * Handle the respawn of a player
     *
     * @param player The player
     */
    public void handleRespawn(Player player) {
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.FFA);
        profile.setFfaMatch(this);

        Arena arena = getArena();

        Bukkit.getScheduler().runTaskLater(Tulip.getInstance(), () -> {
            player.teleport(arena.getSpawn());

            Kit kit = getKit();
            player.getInventory().clear();
            player.getInventory().setHeldItemSlot(0);
            player.getInventory().setContents(profile.getKitLayout().getLayout(kit.getName()) == null ? kit.getItems() : profile.getKitLayout().getLayout(kit.getName()));
            player.getInventory().setArmorContents(kit.getArmor());
            player.updateInventory();
        }, 1L);
    }

    /**
     * Handle the death of a player
     *
     * @param player The player
     * @param killer The killer
     */
    @Override
    public void handleDeath(Player player, Player killer) {
        if (killer == null) {
            Profile playerProfile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            playerProfile.getStats().incrementKitDeaths(getKit());

            getPlayers().forEach(online -> online.sendMessage(CC.translate(ConfigHandler.getInstance().getLocaleConfig().getString("game.killed.no-killer").replace("{player}", player.getName()))));
            handleRespawn(player);
            return;
        }

        CooldownRepository cooldownRepository = Tulip.getInstance().getCooldownRepository();
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));
        if (optionalCooldown.isPresent()) {
            Cooldown cooldown = optionalCooldown.get();
            cooldown.cancelCooldown();
        }

        Profile killerProfile = Tulip.getInstance().getProfileRepository().getProfile(killer.getUniqueId());
        killerProfile.getStats().incrementKitKills(getKit());

        if (killerProfile.getStats().getHighestKillstreak(getKit()) < killerProfile.getStats().getHighestKillstreak(getKit()) + 1) {
            killerProfile.getStats().setHighestKillstreak(getKit(), killerProfile.getStats().getHighestKillstreak(getKit()) + 1);
        }

        killerProfile.getStats().incrementKillstreak(getKit());
        broadcastKillStreak(killer);

        getPlayers().forEach(online -> online.sendMessage(CC.translate(ConfigHandler.getInstance().getLocaleConfig().getString("game.killed.with-killer").replace("{player}", player.getName()).replace("{killer}", killer.getName()))));
        handleRespawn(player);
    }

    /**
     * Alert every five kills
     *
     * @param killer The killer
     */
    private void broadcastKillStreak(Player killer) {
        Profile killerProfile = Tulip.getInstance().getProfileRepository().getProfile(killer.getUniqueId());

        if (killerProfile.getStats().getHighestKillstreak(getKit()) % ConfigHandler.getInstance().getLocaleConfig().getInt("game.killstreak.interval") == 0) {
            for (String message : ConfigHandler.getInstance().getLocaleConfig().getStringList("game.killstreak.broadcast")) {
                getPlayers().forEach(pl -> pl.sendMessage(CC.translate(message).replace("{player}", killer.getName()).replace("{streak}", String.valueOf(killerProfile.getStats().getHighestKillstreak(getKit())))));
            }
        }

        //if (KillStreakData.getCurrentStreak(killer) % ConfigHandler.getInstance().getLocaleConfig().getInt("game.killstreak.interval") == 0) {
        //            for (String message : ConfigHandler.getInstance().getLocaleConfig().getStringList("game.killstreak.broadcast")) {
        //                getPlayers().forEach(pl -> pl.sendMessage(CC.translate(message).replace("{player}", killer.getName()).replace("{streak}", String.valueOf(KillStreakData.getCurrentStreak(killer)))));
        //            }
        //        }
    }
}
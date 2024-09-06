package me.emmy.tulip.ffa.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.hotbar.HotbarUtility;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.ffa.killstreak.KillStreakData;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
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

        player.sendMessage(CC.translate(ConfigHandler.getInstance().getLocaleConfig().getString("game.left")));

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPAWN);
        profile.setFfaMatch(null);

        KillStreakData.resetKillstreak(player);

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

        Profile killerProfile = Tulip.getInstance().getProfileRepository().getProfile(killer.getUniqueId());
        killerProfile.getStats().incrementKitKills(getKit());

        if (KillStreakData.getCurrentStreak(player) != 0) {
            KillStreakData.resetKillstreak(player);
        }

        KillStreakData.incrementKillstreak(killer.getName());
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
        if (KillStreakData.getCurrentStreak(killer) % ConfigHandler.getInstance().getLocaleConfig().getInt("game.killstreak.interval") == 0) {
            for (String message : ConfigHandler.getInstance().getLocaleConfig().getStringList("game.killstreak.broadcast")) {
                getPlayers().forEach(pl -> pl.sendMessage(CC.translate(message).replace("{player}", killer.getName()).replace("{streak}", String.valueOf(KillStreakData.getCurrentStreak(killer)))));
            }
        }
    }
}
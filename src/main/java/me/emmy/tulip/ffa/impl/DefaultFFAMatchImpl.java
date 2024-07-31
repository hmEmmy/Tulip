package me.emmy.tulip.ffa.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
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
            player.sendMessage(CC.translate("&cThis FFA match is full. " + getMaxPlayers() + " players are already in the match."));
            return;
        }

        getPlayers().add(player);
        getPlayers().forEach(online -> online.sendMessage(CC.translate("&a" + player.getName() + " has joined the FFA match.")));
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
        getPlayers().forEach(online -> online.sendMessage(CC.translate("&c" + player.getName() + " has left the FFA match.")));

        player.sendMessage(CC.translate("&aYou have left the FFA match."));

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPAWN);
        profile.setFfaMatch(null);

        KillStreakData.resetKillstreak(player);

        PlayerUtil.reset(player);
        Tulip.getInstance().getSpawnHandler().teleportToSpawn(player);
        HotbarUtility.applyHotbarItems(player);
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
        player.getInventory().setContents(kit.getItems());
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
            player.getInventory().setArmorContents(kit.getArmor());
            player.getInventory().setContents(kit.getItems());
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

            getPlayers().forEach(online -> online.sendMessage(CC.translate("&c" + player.getName() + " has died.")));
            handleRespawn(player);
            return;
        }

        Profile killerProfile = Tulip.getInstance().getProfileRepository().getProfile(killer.getUniqueId());
        killerProfile.getStats().incrementKitKills(getKit());

        if (KillStreakData.getCurrentStreak(player) != 0) {
            KillStreakData.resetKillstreak(player);
        }

        KillStreakData.incrementKillstreak(killer.getName());
        alertEveryFiveKills(killer);

        getPlayers().forEach(online -> online.sendMessage(CC.translate("&c" + player.getName() + " has been killed by " + killer.getName() + ".")));
        handleRespawn(player);
    }

    /**
     * Alert every five kills
     *
     * @param killer The killer
     */
    private void alertEveryFiveKills(Player killer) {
        if (KillStreakData.getCurrentStreak(killer) % 5 == 0) {
            getPlayers().forEach(players -> players.sendMessage(""));
            getPlayers().forEach(players -> players.sendMessage(CC.translate("&a" + killer.getName() + " has reached a killstreak of " + KillStreakData.getCurrentStreak(killer) + ".")));
            getPlayers().forEach(players -> players.sendMessage(""));
        }
    }
}
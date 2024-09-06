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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        List<String> welcomer = new ArrayList<>();
        welcomer.add("");
        welcomer.add("&e&lPlaying FFA");
        welcomer.add(" &e&l● &eKit: &d" + getKit().getName());
        welcomer.add(" &e&l● &eArena: &d" + getArena().getName());
        welcomer.add(" &e&l● &eTo leave, do /leaveffa.");
        welcomer.add("");

        welcomer.forEach(message -> player.sendMessage(CC.translate(message)));

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

        player.sendMessage(CC.translate("&cYou've left the FFA arena."));

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

        if (profile.getKitLayout().getLayout(kit.getName()) == null) {
            player.sendMessage(CC.translate("&aGiving you the default kit layout."));
        } else {
            player.sendMessage(CC.translate("&aGiving you the saved kit layout."));
        }
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
            player.getInventory().setContents(profile.getKitLayout().getLayout(kit.getName()) == null ? kit.getItems() : profile.getKitLayout().getLayout(kit.getName()));

            if (profile.getKitLayout().getLayout(kit.getName()) == null) {
                player.sendMessage(CC.translate("&aGiving you the default kit layout."));
            } else {
                player.sendMessage(CC.translate("&aGiving you the saved kit layout."));
            }
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

            getPlayers().forEach(online -> online.sendMessage(CC.translate("&d" + player.getName() + " &ewas killed.")));
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

        getPlayers().forEach(online -> online.sendMessage(CC.translate("&d" + player.getName() + " &ewas killed by &d" + killer.getName() + "&e.")));
        handleRespawn(player);
    }

    /**
     * Alert every five kills
     *
     * @param killer The killer
     */
    private void alertEveryFiveKills(Player killer) {
        if (KillStreakData.getCurrentStreak(killer) % 5 == 0) {
            Arrays.asList(
                    "",
                    "&d&l" + killer.getName() + " &e&lis on a &d&l" + KillStreakData.getCurrentStreak(killer) + " Kill Streak&e&l!",
                    ""
            ).forEach(message -> getPlayers().forEach(players -> players.sendMessage(CC.translate(message))));
        }
    }
}
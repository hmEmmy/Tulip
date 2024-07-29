package me.emmy.tulip.ffa.safezone.task;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.enums.EnumFFAState;
import me.emmy.tulip.ffa.safezone.cuboid.Cuboid;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Tulip
 * @date 12/06/2024 - 22:26
 */
public class FFASpawnTask extends BukkitRunnable {

    private final Cuboid cuboid;
    private final Tulip plugin;
    private final Map<UUID, EnumFFAState> playerStates;

    public FFASpawnTask(Cuboid cuboid, Tulip plugin) {
        this.cuboid = cuboid;
        this.plugin = plugin;
        this.playerStates = new HashMap<>();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());

            if (profile != null && profile.getState() == EnumProfileState.FFA) {
                EnumFFAState currentState = playerStates.getOrDefault(player.getUniqueId(), EnumFFAState.FIGHTING);
                boolean isInSpawn = cuboid.isIn(player);

                if (isInSpawn && currentState != EnumFFAState.SPAWN) {
                    profile.getFfaMatch().setState(EnumFFAState.SPAWN);
                    playerStates.put(player.getUniqueId(), EnumFFAState.SPAWN);
                    player.sendMessage(CC.translate("&aYou have entered the FFA spawn area."));
                } else if (!isInSpawn && currentState != EnumFFAState.FIGHTING) {
                    profile.getFfaMatch().setState(EnumFFAState.FIGHTING);
                    playerStates.put(player.getUniqueId(), EnumFFAState.FIGHTING);
                    player.sendMessage(CC.translate("&cYou have left the FFA spawn area."));
                }
            }
        }
    }
}
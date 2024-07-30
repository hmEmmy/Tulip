package me.emmy.tulip.ffa.killstreak;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 30/07/2024 - 18:59
 */
@Getter
@UtilityClass
public class KillStreakData {

    private Map<String, Integer> killstreaks = new HashMap<>();

    private int killstreak;

    public void incrementKillstreak(String player) {
        killstreaks.put(player, killstreaks.getOrDefault(player, 0) + 1);
        killstreak = Math.max(killstreak, killstreaks.get(player));
    }

    public void resetKillstreak(Player player) {
        killstreaks.remove(player.getName());
    }

    public int getCurrentStreak(Player player) {
        return killstreaks.getOrDefault(player.getName(), 0);
    }
}

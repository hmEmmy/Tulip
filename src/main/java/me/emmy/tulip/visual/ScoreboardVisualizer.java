package me.emmy.tulip.visual;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.utils.BukkitReflection;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.visual.assemble.AssembleAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 17:42
 */
public class ScoreboardVisualizer implements AssembleAdapter {
    @Override
    public String getTitle(Player player) {
        return CC.translate(ConfigHandler.getInstance().getSettingsConfig().getString("scoreboard.title"));
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> list = new ArrayList<>();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.FFA) {
            for (String line : ConfigHandler.getInstance().getSettingsConfig().getStringList("scoreboard.lines.in-game")) {
                list.add(CC.translate(line)
                        .replace("{sidebar}", "&7&m------------------")
                        .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                        .replace("{kit}", String.valueOf(Tulip.getInstance().getFfaRepository().getFFAMatch(player).getKit().getName()))
                        .replace("{ping}", String.valueOf(BukkitReflection.getPing(player)))
                        .replace("{players}", String.valueOf(profile.getFfaMatch().getPlayers().size()))
                        .replace("{max-online}", String.valueOf(Bukkit.getMaxPlayers()))
                );
            }
        } else {
            for (String line : ConfigHandler.getInstance().getSettingsConfig().getStringList("scoreboard.lines.spawn")) {
                list.add(CC.translate(line)
                        .replace("{sidebar}", "&7&m------------------")
                        .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                        .replace("{max-online}", String.valueOf(Bukkit.getMaxPlayers()))
                );
            }
        }
        return list;
    }
}

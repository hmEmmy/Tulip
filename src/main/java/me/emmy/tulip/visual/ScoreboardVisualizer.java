package me.emmy.tulip.visual;

import me.clip.placeholderapi.PlaceholderAPI;
import me.emmy.tulip.config.ConfigHandler;
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
        for (String line : ConfigHandler.getInstance().getSettingsConfig().getStringList("scoreboard.lines")) {
            String replacedLine = PlaceholderAPI.setPlaceholders(player, line);

            replacedLine = replacedLine.replace("{sidebar}", "&7&m----------------------------")
                    .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("{max-online}", String.valueOf(Bukkit.getMaxPlayers()));
            list.add(CC.translate(replacedLine));
        }
        return list;
    }
}

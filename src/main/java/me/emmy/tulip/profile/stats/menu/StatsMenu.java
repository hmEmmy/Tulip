package me.emmy.tulip.profile.stats.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Tulip
 * @date 31/07/2024 - 13:32
 */
@AllArgsConstructor
public class StatsMenu extends Menu {
    private final FileConfiguration config = ConfigHandler.getInstance().getStatsMenuConfig();
    private OfflinePlayer offlineTargetPlayer;

    @Override
    public String getTitle(Player player) {
        return CC.translate(this.config.getString("title").replace("{player}", this.offlineTargetPlayer.getName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

        buttons.put(4, new GlobalStatsButton(this.offlineTargetPlayer));

        for (AbstractFFAMatch match : Tulip.getInstance().getFfaRepository().getMatches()) {
            buttons.put(slot, new StatsButton(this.offlineTargetPlayer, match));
            slot++;
        }

        this.addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @AllArgsConstructor
    public static class StatsButton extends Button {
        private final FileConfiguration config = ConfigHandler.getInstance().getStatsMenuConfig();
        private OfflinePlayer offlineTargetPlayer;
        private AbstractFFAMatch match;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Tulip.getInstance().getProfileRepository().getProfileWithNoAdding(this.offlineTargetPlayer.getUniqueId());
            if (profile == null) {
                return new ItemBuilder(Material.BARRIER)
                        .name(CC.translate("&c&lError"))
                        .lore(CC.translate("&cThis Player has never played before."))
                        .hideMeta()
                        .build();
            }

            return new ItemBuilder(this.match.getKit().getIcon())
                    .name("&d&l" + this.match.getKit().getName())
                    .durability(this.match.getKit().getIconData())
                    .lore(config.getStringList("player-stats")
                            .stream()
                            .map(s -> s.replace("{kills}", String.valueOf(profile.getStats().getKitKills(this.match.getKit())))
                                    .replace("{deaths}", String.valueOf(profile.getStats().getKitDeaths(this.match.getKit())))
                                    .replace("{ks}", String.valueOf(profile.getStats().getHighestKillstreak(this.match.getKit()))))
                            .map(CC::translate)
                            .collect(Collectors.toList()))
                    .hideMeta()
                    .build();
        }
    }

    @AllArgsConstructor
    public static class GlobalStatsButton extends Button {
        private final FileConfiguration config = ConfigHandler.getInstance().getStatsMenuConfig();
        private OfflinePlayer offlineTargetPlayer;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Tulip.getInstance().getProfileRepository().getProfileWithNoAdding(this.offlineTargetPlayer.getUniqueId());
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            if (profile == null) {
                return new ItemBuilder(Material.BARRIER)
                        .name("&c&lError")
                        .lore("&cThis Player has never played before.")
                        .hideMeta()
                        .build();
            }

            return new ItemBuilder(Material.matchMaterial(config.getString("kit-stats.material")))
                    .name(config.getString("kit-stats.title"))
                    .durability(config.getInt("kit-stats.data"))
                    .lore(config.getStringList("kit-stats.lore")
                            .stream()
                            .map(s -> s.replace("{kills}", String.valueOf(profile.getStats().getTotalKills()))
                                    .replace("{deaths}", String.valueOf(profile.getStats().getTotalDeaths()))
                                    .replace("{kdr}", decimalFormat.format(profile.getStats().getKDR())))
                            .map(CC::translate)
                            .collect(Collectors.toList()))
                    .hideMeta()
                    .build();
        }
    }
}
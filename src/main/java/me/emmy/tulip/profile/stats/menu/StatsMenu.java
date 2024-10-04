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
        return CC.translate(config.getString("title").replace("{player}", offlineTargetPlayer.getName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

        buttons.put(4, new GlobalStatsButton(offlineTargetPlayer));

        for (AbstractFFAMatch match : Tulip.getInstance().getFfaRepository().getMatches()) {
            buttons.put(slot, new StatsButton(offlineTargetPlayer, match));
            slot++;
        }

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @AllArgsConstructor
    public static class StatsButton extends Button {

        private OfflinePlayer offlineTargetPlayer;
        private AbstractFFAMatch match;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Tulip.getInstance().getProfileRepository().getProfileWithNoAdding(offlineTargetPlayer.getUniqueId());
            if (profile == null) {
                return new ItemBuilder(Material.BARRIER)
                        .name(CC.translate("&c&lError"))
                        .lore(CC.translate("&cThis Player has never played before."))
                        .hideMeta()
                        .build();
            }

            return new ItemBuilder(match.getKit().getIcon())
                    .name("&d&l" + match.getKit().getName())
                    .durability(match.getKit().getIconData())
                    .lore(Arrays.asList(
                            "",
                            "&e&l● &eKills: &d" + profile.getStats().getKitKills(match.getKit()),
                            "&e&l● &eDeaths: &d" + profile.getStats().getKitDeaths(match.getKit()),
                            "",
                            "&e&l● &eHighest Killstreak: &d" + profile.getStats().getHighestKillstreak(match.getKit()),
                            ""
                    ))
                    .hideMeta()
                    .build();
        }
    }

    @AllArgsConstructor
    public static class GlobalStatsButton extends Button {

        private OfflinePlayer offlineTargetPlayer;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Tulip.getInstance().getProfileRepository().getProfileWithNoAdding(offlineTargetPlayer.getUniqueId());
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            if (profile == null) {
                return new ItemBuilder(Material.BARRIER)
                        .name("&c&lError")
                        .lore("&cThis Player has never played before.")
                        .hideMeta()
                        .build();
            }

            return new ItemBuilder(Material.NETHER_STAR)
                    .name("&d&lPlayer Stats")
                    .durability(0)
                    .lore(Arrays.asList(
                            "",
                            "&e&l● &eTotal Kills: &d" + profile.getStats().getTotalKills(),
                            "&e&l● &eTotal Deaths: &d" + profile.getStats().getTotalDeaths(),
                            "",
                            "&e&l● &eKill/Death Ratio: &d" + decimalFormat.format(profile.getStats().getKDR()),
                            ""
                    ))
                    .hideMeta()
                    .build();
        }
    }
}
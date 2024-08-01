package me.emmy.tulip.profile.settings.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 01/08/2024 - 14:59
 */
@AllArgsConstructor
public class SettingsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return CC.translate("&e&lSettings");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        buttons.put(10, new SettingsButton(Material.PAPER, "&e&lSidebar Visibility", 0,
                profile.getSettings().isShowScoreboard() ?
                        Arrays.asList(
                                "&7See the scoreboard.",
                                "",
                                " &e&l┃  &aYes",
                                " &e┃ &7No",
                                "",
                                "&eClick to change!"
                        )
                        :
                        Arrays.asList(
                                "&7See the scoreboard.",
                                "",
                                " &e┃ &7Yes",
                                " &e&l┃  &cNo",
                                "",
                                "&eClick to change!"
                        )
        ));

        addBorder(buttons, (byte) 15, 4);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 4;
    }

    @AllArgsConstructor
    public static class SettingsButton extends Button {
        private final Material icon;
        private final String name;
        private int data;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(icon)
                    .name(name)
                    .lore(lore)
                    .durability(data)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) {
                return;
            }

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            switch (icon) {
                case PAPER:
                    profile.getSettings().setShowScoreboard(!profile.getSettings().isShowScoreboard());
                    player.sendMessage(profile.getSettings().isShowScoreboard() ? CC.translate("&aYou can now see the sidebar.") : CC.translate("&cYou can no longer see the sidebar."));
                    break;
            }
        }
    }
}

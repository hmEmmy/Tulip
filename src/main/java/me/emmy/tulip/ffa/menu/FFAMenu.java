package me.emmy.tulip.ffa.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class FFAMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ConfigHandler.getInstance().getGameMenuConfig().getString("title");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (ConfigHandler.getInstance().getGameMenuConfig().getBoolean("glass-border.enabled")) {
            addBorder(buttons, (byte) ConfigHandler.getInstance().getGameMenuConfig().getInt("glass-border.durability"), ConfigHandler.getInstance().getGameMenuConfig().getInt("rows"));

            int slot = 10;

            for (AbstractFFAMatch match : Tulip.getInstance().getFfaRepository().getMatches()) {
                buttons.put(slot++, new FFAButton(match));
                //if (slot >= 54) break;
            }
        } else {
            int slot = 0;

            for (AbstractFFAMatch match : Tulip.getInstance().getFfaRepository().getMatches()) {
                buttons.put(slot++, new FFAButton(match));
                //if (slot >= 54) break;
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return ConfigHandler.getInstance().getGameMenuConfig().getInt("rows") * 9;
    }

    @AllArgsConstructor
    public static class FFAButton extends Button {

        private final AbstractFFAMatch match;

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            for (String line : ConfigHandler.getInstance().getGameMenuConfig().getStringList("button.lore")) {
                lore.add(line
                        .replace("{players}", String.valueOf(match.getPlayers().size()))
                        .replace("{max-players}", String.valueOf(match.getMaxPlayers()))
                        .replace("{arena}", match.getArena().getName())
                        .replace("{description}", match.getKit().getDescription())
                        .replace("{kit}", match.getKit().getName()));
            }

            return new ItemBuilder(match.getKit().getIcon())
                    .name(ConfigHandler.getInstance().getGameMenuConfig().getString("button.name")
                            .replace("{kit}", match.getKit().getName()))
                    .durability(match.getKit().getIconData())
                    .lore(lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType != ClickType.LEFT) return;
            playSuccess(player);
            match.join(player);
        }
    }
}

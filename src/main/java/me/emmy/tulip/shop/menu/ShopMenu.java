package me.emmy.tulip.shop.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 08/09/2024 - 14:57
 */
@AllArgsConstructor
public class ShopMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Collections.emptyMap();
    }

    @AllArgsConstructor
    public static class ShopMenuButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return null;
        }
    }
}

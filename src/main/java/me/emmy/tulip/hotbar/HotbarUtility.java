package me.emmy.tulip.hotbar;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 28/07/2024 - 23:12
 */
@UtilityClass
public class HotbarUtility {
    /**
     * Apply hotbar items.
     *
     * @param player the player
     */
    public void applyHotbarItems(Player player) {
        for (HotbarItemBuilder hotbarItemBuilder : HotbarItemBuilder.values()) {
            //HotbarItemBuilder.reloadConfig();
            player.getInventory().setItem(hotbarItemBuilder.getSlot(), hotbarItemBuilder.createItem());
        }
    }

    /**
     * Remove only the hotbar items.
     *
     * @param player the player
     */
    public void removeHotbarItems(Player player) {
        for (HotbarItemBuilder hotbarItemBuilder : HotbarItemBuilder.values()) {
            player.getInventory().setItem(hotbarItemBuilder.getSlot(), null);
        }
    }
}

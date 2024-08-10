package me.emmy.tulip.profile.kitlayout;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Emmy
 * @project Tulip
 * @date 10/08/2024 - 18:43
 */
@Getter
@Setter
public class ProfileKitLayout {

    private boolean isEditing;

    private Map<String, ItemStack[]> kitItems = new HashMap<>();

    /**
     * Sets the layout for a specific kit.
     */
    public void setLayout(String kitName, ItemStack[] items) {
        this.kitItems.put(kitName, items);
    }

    /**
     * Gets the items for a specific kit.
     */
    public ItemStack[] getLayout(String kitName) {
        return this.kitItems.getOrDefault(kitName, new ItemStack[0]);
    }
}

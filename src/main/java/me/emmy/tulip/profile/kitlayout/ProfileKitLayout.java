package me.emmy.tulip.profile.kitlayout;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.kit.Kit;
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
     * Set the layout for a specific kit by the kit name.
     *
     * @param kitName The name of the kit.
     * @param items The items to set.
     */
    public void setLayout(String kitName, ItemStack[] items) {
        this.kitItems.put(kitName, items);
    }

    /**
     * Set the layout for a specific kit by the kit object.
     *
     * @param kit The kit.
     * @param items The items to set.
     */
    public void setLayout(Kit kit, ItemStack[] items) {
        this.kitItems.put(kit.getName(), items);
    }

    /**
     * Get the layout for a specific kit by the kit name.
     *
     * @param kitName The name of the kit.
     * @return The layout for the kit.
     */
    public ItemStack[] getLayout(String kitName) {
        return this.kitItems.getOrDefault(kitName, new ItemStack[0]);
    }

    /**
     * Get the layout for a specific kit by the kit object.
     *
     * @param kit The kit.
     * @return The layout for the kit.
     */
    public ItemStack[] getLayout(Kit kit) {
        return this.kitItems.getOrDefault(kit.getName(), new ItemStack[0]);
    }
}

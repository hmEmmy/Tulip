package me.emmy.tulip.api.menu.button;

import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.utils.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Emmy
 * Project: FlowerCore
 * Discord: dsc.gg/emmiesa
 */

public class RefillGlassButton extends Button {

    private final Material material;
    private final short data;

    public RefillGlassButton(Material material, int data) {
        this.material = material;
        this.data = (short) data;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(material, 1, data);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(CC.translate(" "));

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        // Handle the refill glass button click event if needed
    }
}

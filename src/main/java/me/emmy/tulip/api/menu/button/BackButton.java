package me.emmy.tulip.api.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class BackButton extends Button {

    private Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&c&lBack")
                .durability(0)
                .lore(Arrays.asList(
                        "&cClick here to return to",
                        "&cthe previous menu.")
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        playDoor(player);
        back.openMenu(player);
    }
}

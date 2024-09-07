package me.emmy.tulip.api.menu.pagination;

import lombok.AllArgsConstructor;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class PageButton extends Button {

    private int mod;
    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                        .name(CC.translate("&bNext Page"))
                        .lore(Arrays.asList(
                                CC.translate("&aClick here to jump"),
                                CC.translate("&ato the next page.")
                        ))
                        .hideMeta()
                        .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                        .name(CC.translate("&cNext Page"))
                        .lore(Arrays.asList(
                                ChatColor.RED + "There is no available",
                                ChatColor.RED + "next page."
                        ))
                        .hideMeta()
                        .build();
            }
        } else {
            if (hasPrevious(player)) {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                        .name(CC.translate("&6Previous Page"))
                        .lore(Arrays.asList(
                                ChatColor.GOLD + "Click here to jump",
                                ChatColor.GOLD + "to the previous page."
                        ))
                        .hideMeta()
                        .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                        .name(CC.translate("&cPrevious Page"))
                        .lore(Arrays.asList(
                                ChatColor.RED + "There is no available",
                                ChatColor.RED + "previous page."
                        ))
                        .hideMeta()
                        .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        } else {
            if (hasPrevious(player)) {
                this.menu.modPage(player, this.mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return this.menu.getPages(player) >= pg;
    }

    private boolean hasPrevious(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0;
    }

}

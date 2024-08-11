package me.emmy.tulip.ffa.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Tulip
 * @date 23/05/2024 - 01:29
 */
@AllArgsConstructor
public class FFAButton extends Button {

    private final AbstractFFAMatch match;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add("&f&l● &fPlayers: &d" + match.getPlayers().size() + "/" + match.getMaxPlayers());
        lore.add("&f&l● &fArena: &d" + match.getArena().getName());
        lore.add("&f&l● &fKit: &d" + match.getKit().getName());
        lore.add("");
        lore.add("&fClick to play!");

        return new ItemBuilder(match.getKit().getIcon())
                .name("&d&l" + match.getName())
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

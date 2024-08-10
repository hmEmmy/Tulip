package me.emmy.tulip.profile.kitlayout.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.hotbar.HotbarUtility;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.kit.KitRepository;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.ItemBuilder;
import me.emmy.tulip.utils.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 10/08/2024 - 19:59
 */
@AllArgsConstructor
public class KitLayoutSelectionMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eSelect a Kit to Edit");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        addBorder(buttons, (byte) 15, 5);

        KitRepository kitRepository = Tulip.getInstance().getKitRepository();
        int slot = 10;

        for (Kit kit : kitRepository.getKits()) {
            buttons.put(slot++, new KitButton(kit));
            //if (slot >= 54) break;
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @AllArgsConstructor
    public static class KitButton extends Button {

        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(kit.getIcon())
                    .name(CC.translate("&d" + kit.getName()))
                    .lore(CC.translate("&7" + kit.getDescription()))
                    .build();
        }
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            profile.getKitLayout().setEditing(true);

            new KitLayoutEditorMenu(kit).openMenu(player);
            player.getInventory().setContents(kit.getItems());
            player.getInventory().setArmorContents(kit.getArmor());
        }
    }

    @Override
    public boolean isUpdateAfterClick() {
        return false;
    }

    @Override
    public void onClose(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        HotbarUtility.applyHotbarItems(player);
    }
}

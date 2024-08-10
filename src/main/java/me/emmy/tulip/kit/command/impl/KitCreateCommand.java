package me.emmy.tulip.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 22:26
 */
public class KitCreateCommand extends BaseCommand {
    @Override
    @Command(name = "kit.create", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit create (name)"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) != null) {
            player.sendMessage(CC.translate("&cA kit with that name already exists."));
            return;
        }

        ItemStack[] items = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();

        Tulip.getInstance().getKitRepository().createKit(name, "&eDescription of the &d" + name + " &ekit", items, armor, Material.DIAMOND_AXE, 0, true);
        Tulip.getInstance().getKitRepository().saveKit(name);

        player.sendMessage(CC.translate("&aKit &b" + name + " &ahas been created."));
        player.sendMessage(CC.translate(" &7TIP: Your current inventory will be set as the kit's inventory content. You can change it by using &b/kit setinv " + name + "&7&o."));
    }
}

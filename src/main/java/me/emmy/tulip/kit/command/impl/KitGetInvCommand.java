package me.emmy.tulip.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 10:39
 */
public class KitGetInvCommand extends BaseCommand {
    @Override
    @Command(name = "kit.getinv", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit getinv (name)"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist."));
            return;
        }

        player.getInventory().setContents(Tulip.getInstance().getKitRepository().getKit(name).getItems());
        player.getInventory().setArmorContents(Tulip.getInstance().getKitRepository().getKit(name).getArmor());
        player.sendMessage(CC.translate("&aKit " + name + " inventory has been given."));
    }
}

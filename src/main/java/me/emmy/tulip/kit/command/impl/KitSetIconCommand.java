package me.emmy.tulip.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 11:18
 */
public class KitSetIconCommand extends BaseCommand {
    @Override
    @Command(name = "kit.seticon", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit seticon (name)"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist."));
            return;
        }

        Tulip.getInstance().getKitRepository().getKit(name).setIcon(player.getItemInHand().getType());
        Tulip.getInstance().getKitRepository().getKit(name).setIconData(player.getItemInHand().getDurability());
        Tulip.getInstance().getKitRepository().saveKit(name);

        player.sendMessage(CC.translate("&aKit &b" + name + " &aicon has been set."));
    }
}

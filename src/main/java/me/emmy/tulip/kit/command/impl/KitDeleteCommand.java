package me.emmy.tulip.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 22:31
 */
public class KitDeleteCommand extends BaseCommand {
    @Override
    @Command(name = "kit.delete", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit delete (name)"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist."));
            return;
        }

        Kit kit = Tulip.getInstance().getKitRepository().getKit(name);
        Tulip.getInstance().getKitRepository().deleteKit(kit);
        player.sendMessage(CC.translate("&aKit " + name + " has been deleted."));
    }
}

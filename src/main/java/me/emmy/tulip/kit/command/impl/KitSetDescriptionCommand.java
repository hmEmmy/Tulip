package me.emmy.tulip.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 11:12
 */
public class KitSetDescriptionCommand extends BaseCommand {
    @Override
    @Command(name = "kit.setdescription",aliases = "kit.setdesc", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /kit setdescription (name) (description)"));
            return;
        }

        String name = args[0];
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist."));
            return;
        }

        Tulip.getInstance().getKitRepository().getKit(name).setDescription(description);
        Tulip.getInstance().getKitRepository().saveKit(name);

        player.sendMessage(CC.translate("&aKit " + name + " description has been set."));
    }
}

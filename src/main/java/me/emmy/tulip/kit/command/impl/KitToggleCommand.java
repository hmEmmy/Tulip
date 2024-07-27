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
 * @date 27/07/2024 - 10:40
 */
public class KitToggleCommand extends BaseCommand {
    @Override
    @Command(name = "kit.toggle", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit toggle (name)"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist."));
            return;
        }

        Tulip.getInstance().getKitRepository().getKit(name).setEnabled(!Tulip.getInstance().getKitRepository().getKit(name).isEnabled());
        Tulip.getInstance().getKitRepository().saveKit(name);

        player.sendMessage(CC.translate("&aKit " + name + " has been " + (Tulip.getInstance().getKitRepository().getKit(name).isEnabled() ? "enabled" : "disabled") + "."));
    }
}

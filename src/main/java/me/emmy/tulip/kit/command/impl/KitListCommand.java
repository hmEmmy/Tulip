package me.emmy.tulip.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 10:37
 */
public class KitListCommand extends BaseCommand {
    @Override
    @Command(name = "kit.list", permission = "Tulip.admin", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&eKit List &7(&e" + Tulip.getInstance().getKitRepository().getKits().size() + "&7)"));
        if (Tulip.getInstance().getKitRepository().getKits().isEmpty()) {
            sender.sendMessage(CC.translate("&7There are no kits available."));
        }
        Tulip.getInstance().getKitRepository().getKits().forEach(arena -> sender.sendMessage(CC.translate(" &7- " + arena.getName())));
        sender.sendMessage("");
    }
}

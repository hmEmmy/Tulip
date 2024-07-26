package me.emmy.tulip.kit.command;

import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 22:13
 */
public class KitCommand extends BaseCommand {
    @Override
    @Command(name = "kit", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&eKit Creation Help"));
        player.sendMessage(CC.translate("&7- &e/kit create &7<name>"));
        player.sendMessage(CC.translate("&7- &e/kit delete &7<name>"));
        player.sendMessage(CC.translate("&7- &e/kit info &7<name>"));
        player.sendMessage(CC.translate("&7- &e/kit list"));
        player.sendMessage(CC.translate("&7- &e/kit setinv &7<name>"));
        player.sendMessage(CC.translate("&7- &e/kit getinv &7<name>"));
        player.sendMessage(CC.translate("&7- &e/kit save"));
        player.sendMessage(CC.translate("&7- &e/kit toggle &7<name>"));
        player.sendMessage("");
    }
}

package me.emmy.tulip.kit.command;

import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
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
        player.sendMessage(CC.translate("&dKit Creation Help"));
        player.sendMessage(CC.translate("&7- &d/kit create &7<name>"));
        player.sendMessage(CC.translate("&7- &d/kit delete &7<name>"));
        player.sendMessage(CC.translate("&7- &d/kit info &7<name>"));
        player.sendMessage(CC.translate("&7- &d/kit list"));
        player.sendMessage(CC.translate("&7- &d/kit setinv &7<name>"));
        player.sendMessage(CC.translate("&7- &d/kit getinv &7<name>"));
        player.sendMessage(CC.translate("&7- &d/kit seticon &7<name>"));
        player.sendMessage(CC.translate("&7- &d/kit toggle &7<name>"));
        player.sendMessage("");
    }
}

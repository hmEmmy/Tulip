package me.emmy.tulip.game.command;

import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 13:27
 */
public class GameCommand extends BaseCommand {
    @Override
    @Command(name = "game", permission = "tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&eGame Creation Help"));
        player.sendMessage(CC.translate("&7- &e/game create &7<name> &7<kit> &7<arena>"));
        player.sendMessage(CC.translate("&7- &e/game delete &7<name>"));
        player.sendMessage(CC.translate("&7- &e/game info &7<name>"));
        player.sendMessage(CC.translate("&7- &e/game list"));
        player.sendMessage(CC.translate("&7- &e/game setkit &7<name> &7<kit>"));
        player.sendMessage(CC.translate("&7- &e/game setarena &7<name> &7<arena>"));
        player.sendMessage(CC.translate("&7- &e/game allowmultiplekits &7<name> &7<true/false>"));
        player.sendMessage("");
    }
}

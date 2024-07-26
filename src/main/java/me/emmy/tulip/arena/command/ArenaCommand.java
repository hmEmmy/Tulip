package me.emmy.tulip.arena.command;

import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 19:02
 */
public class ArenaCommand extends BaseCommand {
    @Override
    @Command(name = "arena", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&eArena Creation Help"));
        player.sendMessage(CC.translate("&7- &e/arena create &7<name>"));
        player.sendMessage(CC.translate("&7- &e/arena delete &7<name>"));
        player.sendMessage(CC.translate("&7- &e/arena info &7<name>"));
        player.sendMessage(CC.translate("&7- &e/arena list"));
        player.sendMessage(CC.translate("&7- &e/arena setcenter &7<name>"));
        player.sendMessage(CC.translate("&7- &e/arena setmaxplayers &7<name> <maxPlayers>"));
        player.sendMessage(CC.translate("&7- &e/arena setminplayers &7<name> <minPlayers>"));
        player.sendMessage(CC.translate("&7- &e/arena setspawn &7<name>"));
        player.sendMessage(CC.translate("&7- &e/arena teleport &7<name>"));
        player.sendMessage("");
    }
}

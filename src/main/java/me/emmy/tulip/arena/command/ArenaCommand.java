package me.emmy.tulip.arena.command;

import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
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
        player.sendMessage(CC.translate("&dArena Creation Help"));
        player.sendMessage(CC.translate("&7- &d/arena create &7<name>"));
        player.sendMessage(CC.translate("&7- &d/arena delete &7<name>"));
        player.sendMessage(CC.translate("&7- &d/arena info &7<name>"));
        player.sendMessage(CC.translate("&7- &d/arena list"));
        player.sendMessage(CC.translate("&7- &d/arena setcenter &7<name>"));
        player.sendMessage(CC.translate("&7- &d/arena setmaxplayers &7<name> <maxPlayers>"));
        player.sendMessage(CC.translate("&7- &d/arena setminplayers &7<name> <minPlayers>"));
        player.sendMessage(CC.translate("&7- &d/arena setspawn &7<name>"));
        player.sendMessage(CC.translate("&7- &d/arena teleport &7<name>"));
        player.sendMessage("");
    }
}

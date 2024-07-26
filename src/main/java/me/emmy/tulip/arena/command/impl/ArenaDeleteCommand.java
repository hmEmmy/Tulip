package me.emmy.tulip.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 19:29
 */
public class ArenaDeleteCommand extends BaseCommand {
    @Override
    @Command(name = "arena.delete", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena delete <name>"));
            return;
        }

        String arenaName = args[0];

        if (Tulip.getInstance().getArenaRepository().getArena(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        Tulip.getInstance().getArenaRepository().deleteArena(arenaName);
        player.sendMessage(CC.translate("&cArena &b" + arenaName + " &chas been deleted!"));
    }
}

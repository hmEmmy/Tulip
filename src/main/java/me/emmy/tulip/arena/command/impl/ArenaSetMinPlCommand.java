package me.emmy.tulip.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.annotation.Command;
import me.emmy.tulip.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 19:33
 */
public class ArenaSetMinPlCommand extends BaseCommand {
    @Override
    @Command(name = "arena.setminplayers", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            player.sendMessage(CC.translate("&cUsage: /arena setminpl <name> <minPlayers>"));
            return;
        }

        String arenaName = args[0];

        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
        if (arena == null) {
            player.sendMessage("&cAn arena with that name does not exist!");
            return;
        }

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cPlease enter a valid number!"));
            return;
        }

        Tulip.getInstance().getArenaRepository().getArena(arenaName).setMinPlayers(Integer.parseInt(args[1]));
        Tulip.getInstance().getArenaRepository().saveArena(arena);
        player.sendMessage(CC.translate("&aMin players set to " + args[1] + " for arena " + arenaName + "!"));
    }
}

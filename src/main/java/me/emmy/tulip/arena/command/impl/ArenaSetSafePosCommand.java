package me.emmy.tulip.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 12:35
 */
public class ArenaSetSafePosCommand extends BaseCommand {
    @Override
    @Command(name = "arena.setsafepos", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /arena setsafepos (name) (1/2)"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getArenaRepository().getArena(name) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cPlease enter a valid number!"));
            return;
        }

        if (Integer.parseInt(args[1]) != 1 && Integer.parseInt(args[1]) != 2) {
            player.sendMessage(CC.translate("&cPlease enter a valid number!"));
            return;
        }

        if (args[1].equals("1")) {
            Tulip.getInstance().getArenaRepository().getArena(name).setSafePos1(player.getLocation());
            player.sendMessage(CC.translate("&aSafe position 1 set for arena " + name + "!"));
            return;
        }

        if (args[1].equals("2")) {
            Tulip.getInstance().getArenaRepository().getArena(name).setSafePos2(player.getLocation());
            player.sendMessage(CC.translate("&aSafe position 2 set for arena " + name + "!"));
            return;
        }

        Tulip.getInstance().getArenaRepository().saveArena(Tulip.getInstance().getArenaRepository().getArena(name));
    }
}

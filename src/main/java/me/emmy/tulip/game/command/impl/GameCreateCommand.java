package me.emmy.tulip.game.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.kit.Kit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 13:36
 */
public class GameCreateCommand extends BaseCommand {
    @Override
    @Command(name = "game.create", permission = "tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 3) {
            player.sendMessage(CC.translate("&cUsage: /game create (name) (arena) (allowMultipleKits)"));
            return;
        }

        String name = args[0];
        String arenaName = args[1];
        boolean allowMultipleKits;

        try {
            allowMultipleKits = Boolean.parseBoolean(args[2]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid value for allowMultipleKits. Must be true or false."));
            return;
        }

        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(CC.translate("&cArena not found."));
            return;
        }

        Tulip.getInstance().getGameRepository().createGame(name, arena, null, allowMultipleKits);
        player.sendMessage(CC.translate("&aGame created successfully!"));
    }
}

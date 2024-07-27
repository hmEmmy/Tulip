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

        if (args.length < 4) {
            player.sendMessage(CC.translate("&cUsage: /game create (name) (kit) (arena) (allowMultipleKits)"));
            return;
        }

        String name = args[0];
        String kitName = args[1];
        String arenaName = args[2];
        boolean allowMultipleKits;

        try {
            allowMultipleKits = Boolean.parseBoolean(args[3]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid value for allowMultipleKits. Must be true or false."));
            return;
        }

        Kit kit = Tulip.getInstance().getKitRepository().getKit(kitName);
        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);

        if (kit == null) {
            player.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        if (arena == null) {
            player.sendMessage(CC.translate("&cArena not found."));
            return;
        }

        Tulip.getInstance().getGameRepository().createGame(name, arena, kit, allowMultipleKits);
        player.sendMessage(CC.translate("&aGame created successfully!"));
    }
}

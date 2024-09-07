package me.emmy.tulip.ffa.command.admin.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 5/27/2024
 */
public class FFACreateCommand extends BaseCommand {
    @Command(name = "ffa.create", permission = "Tulip.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa create &b<arena> <kit> <maxPlayers>"));
            return;
        }

        String arenaName = args[0];
        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(CC.translate("&cArena not found."));
            return;
        }

        String kitName = args[1];
        Kit kit = Tulip.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        int maxPlayers = Integer.parseInt(args[2]);

        if (Tulip.getInstance().getFfaRepository().getFFAMatch(kitName) != null) {
            player.sendMessage(CC.translate("&cThere is already a FFA match with the name " + kitName + "."));
            return;
        }

        if (!Tulip.getInstance().getKitRepository().getKit(kitName).isEnabled()) {
            player.sendMessage(CC.translate("&cThe kit " + kitName + " is disabled."));
            return;
        }

        Tulip.getInstance().getFfaRepository().createFFAMatch(arena, kit, maxPlayers);
        player.sendMessage(CC.translate("&aSuccessfully created the FFA match."));
    }
}

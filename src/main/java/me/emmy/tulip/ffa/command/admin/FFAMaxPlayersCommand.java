package me.emmy.tulip.ffa.command.admin;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.AbstractFFAMatch;
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
public class FFAMaxPlayersCommand extends BaseCommand {
    @Command(name = "ffa.maxplayers", permission = "Tulip.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa maxplayers &b<kit> <maxPlayers>"));
            return;
        }

        String kitName = args[0];
        int maxPlayers = Integer.parseInt(args[1]);

        AbstractFFAMatch match = Tulip.getInstance().getFfaRepository().getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate("&cThere is no FFA match with the name " + kitName + "."));
            return;
        }

        match.setMaxPlayers(maxPlayers);
        Tulip.getInstance().getFfaRepository().saveFFAMatch(match);
        player.sendMessage(CC.translate("&aSuccessfully set the max players for the FFA match."));
    }
}

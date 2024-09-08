package me.emmy.tulip.ffa.command.admin.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.locale.Locale;
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
public class FFAKickCommand extends BaseCommand {
    @Command(name = "ffa.kick", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        // /ffa kick <player>

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa kick &b<player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThere is no player with the name " + args[0] + "."));
            return;
        }

        AbstractFFAMatch match = Tulip.getInstance().getFfaRepository().getFFAMatch(target);
        if (match == null) {
            player.sendMessage(CC.translate(Locale.FFA_PLAYER_NOT_IN_MATCH.getStringPath()).replace("{player}", target.getName()));
            return;
        }

        match.leave(target);
        target.sendMessage(CC.translate(Locale.FFA_KICKED.getStringPath()));
        player.sendMessage(CC.translate(Locale.FFA_KICKED_PLAYER.getStringPath()).replace("{player}", target.getName()));
    }
}

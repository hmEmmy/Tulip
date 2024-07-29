package me.emmy.tulip.ffa.command.admin;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.ffa.FFARepository;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 5/27/2024
 */
public class FFAListCommand extends BaseCommand {
    @Command(name = "ffa.list", permission = "Tulip.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage("");
        player.sendMessage(CC.translate("     &d&lFFA Match List &f(" + Tulip.getInstance().getFfaRepository().getMatches().size() + "&f)"));
        if (Tulip.getInstance().getFfaRepository().getMatches().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Matches available."));
        }
        Tulip.getInstance().getFfaRepository().getMatches().forEach(match -> player.sendMessage(CC.translate("      &f● &d" + match.getKit().getName() + " &f(" + (match.getPlayers().size() + "/" + match.getMaxPlayers()) + "&f)")));
        player.sendMessage("");
    }
}

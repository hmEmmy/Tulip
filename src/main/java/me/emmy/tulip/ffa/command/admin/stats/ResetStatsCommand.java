package me.emmy.tulip.ffa.command.admin.stats;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 30/07/2024 - 22:53
 */
public class ResetStatsCommand extends BaseCommand {
    @Override
    @Command(name = "resetstats", permission = "tulip.command.resetstats", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: /resetstats (player)"));
            return;
        }

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);

        if (target == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        profile.setKills(0);
        profile.setDeaths(0);
        sender.sendMessage(CC.translate("&aYou have reset the stats of " + target.getName() + "."));
    }
}

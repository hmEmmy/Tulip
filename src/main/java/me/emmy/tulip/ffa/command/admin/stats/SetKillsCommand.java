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
 * @date 30/07/2024 - 22:50
 */
public class SetKillsCommand extends BaseCommand {
    @Override
    @Command(name = "setkills", permission = "tulip.command.setkills", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: /setkills (player) (kills)"));
            return;
        }

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }
        int kills;

        try {
            kills = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        profile.setKills(kills);
        sender.sendMessage(CC.translate("&aYou have set the kills of " + target.getName() + " to " + kills + "."));
    }
}

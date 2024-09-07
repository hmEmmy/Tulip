package me.emmy.tulip.ffa.killstreak.command;

import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.ffa.killstreak.KillStreakData;
import me.emmy.tulip.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 31/07/2024 - 15:28
 */
public class SetKillStreakCommand extends BaseCommand {
    @Override
    @Command(name = "setkillstreak", permission = "tulip.command.setkillstreak", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: /setkillstreak (player) (value)"));
            return;
        }

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        int value;

        try {
            value = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        KillStreakData.setKillStreak(target, value);
        sender.sendMessage(CC.translate("&aYou have set the killstreak of " + target.getName() + " to " + value + "."));
    }
}

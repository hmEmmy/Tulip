package me.emmy.tulip.essential.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 17:55
 */
public class TulipCommand extends BaseCommand {
    @Override
    @Command(name = "tulip", aliases = {"ffa", "ffacore"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&e&lTulip &7- &fFFA Core made by &e" + Tulip.getInstance().getDescription().getAuthors() + "&f.").replace("[", "").replace("]", ""));
        sender.sendMessage(CC.translate(" &fVersion: &e" + Tulip.getInstance().getDescription().getVersion()));
        sender.sendMessage(CC.translate(" &fGithub: &ehttps://github.com/hmEmmy/Tulip"));
        sender.sendMessage(CC.translate(" &fDiscord: &ehttps://dsc.gg/dulcy"));
        sender.sendMessage("");
    }
}

package me.emmy.tulip.hotbar.command;

import me.emmy.tulip.hotbar.HotbarUtility;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 28/07/2024 - 21:07
 */
public class RemoveHotbarItemsCommand extends BaseCommand {
    @Override
    @Command(name = "removehotbaritems", permission = "tulip.command.removehotbaritems")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            HotbarUtility.removeHotbarItems(player);
            player.sendMessage(CC.translate("&aYou have removed all hotbar items from your inventory."));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        HotbarUtility.removeHotbarItems(player);
        player.sendMessage(CC.translate("&aYou have removed all hotbar items from &e" + targetPlayer.getName() + "'s &ainventory."));
        targetPlayer.sendMessage(CC.translate("&aYour hotbar items have been removed from your inventory."));
    }
}

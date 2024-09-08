package me.emmy.tulip.shop.command;

import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 08/09/2024 - 14:58
 */
public class ShopCommand extends BaseCommand {
    @Command(name = "shop")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&e&o&lCurrently in development!"));
    }
}

package me.emmy.tulip.spawn.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 29/07/2024 - 23:09
 */
public class SetSpawnCommand extends BaseCommand {
    @Override
    @Command(name = "setspawn", permission = "tulip.command.setspawn")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Tulip.getInstance().getSpawnHandler().setSpawn(player.getLocation());
        player.sendMessage(CC.translate(Locale.SPAWN_SET.getStringPath()));
    }
}

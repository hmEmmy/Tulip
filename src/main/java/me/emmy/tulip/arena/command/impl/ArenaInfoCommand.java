package me.emmy.tulip.arena.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Tulip
 * Date: 05/06/2024 - 20:35
 */
public class ArenaInfoCommand extends BaseCommand {
    @Override
    @Command(name = "arena.info", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /arena info <name>"));
            return;
        }

        String arenaName = args[0];

        Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);

        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&d&lArena Information"));
        player.sendMessage(CC.translate(" &7- &dArena Name: &7"+ arena.getName()));
        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate(" &7- &dCenter: &cNot set"));
        } else {
            player.sendMessage(CC.translate(" &7- &dCenter: &7" + arena.getCenter().getWorld().getName() + "&d:"));
            player.sendMessage(CC.translate("   &7* &dX: &7" + arena.getCenter().getBlockX()));
            player.sendMessage(CC.translate("   &7* &dY: &7" + arena.getCenter().getBlockY()));
            player.sendMessage(CC.translate("   &7* &dZ: &7" + arena.getCenter().getBlockZ()));
            if (ConfigHandler.getInstance().getSettingsConfig().getBoolean("arena.extend-info-command")) {
                player.sendMessage(CC.translate("   &7* &dPitch: &7" + arena.getCenter().getPitch()));
                player.sendMessage(CC.translate("   &7* &dYaw: &7" + arena.getCenter().getYaw()));
            }
        }

        if (arena.getSpawn() == null) {
            player.sendMessage(CC.translate(" &7- &dSpawn: &cNot set"));
        } else {
            player.sendMessage(CC.translate(" &7- &dSpawn: &7" + arena.getSpawn().getWorld().getName() + "&d:"));
            player.sendMessage(CC.translate("   &7* &dX: &7" + arena.getSpawn().getBlockX()));
            player.sendMessage(CC.translate("   &7* &dY: &7" + arena.getSpawn().getBlockY()));
            player.sendMessage(CC.translate("   &7* &dZ: &7" + arena.getSpawn().getBlockZ()));
            if (ConfigHandler.getInstance().getSettingsConfig().getBoolean("arena.extend-info-command")) {
                player.sendMessage(CC.translate("   &7* &dPitch: &7" + arena.getSpawn().getPitch()));
                player.sendMessage(CC.translate("   &7* &dYaw: &7" + arena.getSpawn().getYaw()));
            }
        }
        player.sendMessage("");
    }
}

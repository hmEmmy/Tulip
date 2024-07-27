package me.emmy.tulip.kit.command.impl;

import me.clip.placeholderapi.libs.kyori.adventure.platform.viaversion.ViaFacet;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.kit.KitRepository;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.command.BaseCommand;
import me.emmy.tulip.utils.command.CommandArgs;
import me.emmy.tulip.utils.command.annotation.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 10:38
 */
public class KitSetInvCommand extends BaseCommand {
    @Override
    @Command(name = "kit.setinv", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /kit setinv (name)"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist."));
            return;
        }

        KitRepository kitRepository = Tulip.getInstance().getKitRepository();

        kitRepository.getKit(name).setItems(player.getInventory().getContents());
        kitRepository.getKit(name).setArmor(player.getInventory().getArmorContents());
        kitRepository.saveKit(name);

        player.sendMessage(CC.translate("&aKit " + name + " inventory has been set."));
    }
}

package me.emmy.tulip.ffa.command.admin;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.menu.FFAMenu;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.utils.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 01/06/2024 - 00:14
 */
public class PlayCommand extends BaseCommand {
    @Override
    @Command(name = "play")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            player.sendMessage(CC.translate("&cYou are already in a match."));
            return;
        }

        new FFAMenu().openMenu(player);
    }
}

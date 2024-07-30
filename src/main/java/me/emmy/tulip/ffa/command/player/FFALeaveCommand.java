package me.emmy.tulip.ffa.command.player;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 5/27/2024
 */
public class FFALeaveCommand extends BaseCommand {
    @Command(name = "ffa.leave", aliases = "leaveffa")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getFfaMatch() == null) {
            player.sendMessage(CC.translate("&cYou are not in a FFA match."));
            return;
        }

        profile.getFfaMatch().leave(player);
    }
}

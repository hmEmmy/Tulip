package me.emmy.tulip.profile.settings.command.toggle;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 01/08/2024 - 14:44
 */
public class ToggleScoreboardCommand extends BaseCommand {
    @Override
    @Command(name = "togglescoreboard", aliases = {"tsb"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        FileConfiguration config = ConfigHandler.getInstance().getLocaleConfig();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.getSettings().setShowScoreboard(!profile.getSettings().isShowScoreboard());
        player.sendMessage(profile.getSettings().isShowScoreboard() ? CC.translate(config.getString("profile-settings.sidebar.enabled")) : CC.translate(config.getString("profile-settings.sidebar.disabled")));
    }
}

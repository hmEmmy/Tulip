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
 * @date 07/09/2024 - 16:08
 */
public class ToggleTablistCommand extends BaseCommand {
    @Command(name = "toggletablist")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        FileConfiguration config = ConfigHandler.getInstance().getLocaleConfig();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.getSettings().setShowTablist(!profile.getSettings().isShowTablist());
        player.sendMessage(profile.getSettings().isShowTablist() ? CC.translate(config.getString("profile-settings.tablist.enabled")) : CC.translate(config.getString("profile-settings.tablist.disabled")));

    }
}

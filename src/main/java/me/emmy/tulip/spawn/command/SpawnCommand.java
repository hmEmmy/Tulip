package me.emmy.tulip.spawn.command;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.ffa.FFARepository;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.utils.PlayerUtil;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 29/07/2024 - 23:09
 */
public class SpawnCommand extends BaseCommand {
    @Override
    @Command(name = "spawn", permission = "tulip.command.spawn")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            AbstractFFAMatch ffaMatch = Tulip.getInstance().getFfaRepository().getFFAMatch(player);
            if (ffaMatch != null) {
                ffaMatch.leave(player);
                return;
            }
        }

        Tulip.getInstance().getSpawnHandler().teleportToSpawn(player);
        PlayerUtil.reset(player);
    }
}

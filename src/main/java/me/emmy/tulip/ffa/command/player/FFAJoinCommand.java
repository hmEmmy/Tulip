package me.emmy.tulip.ffa.command.player;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.FFARepository;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 5/27/2024
 */
public class FFAJoinCommand extends BaseCommand {
    @Command(name = "ffa.join")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage("Usage: /ffa join <kit>");
            return;
        }

        String kitName = args[0];
        Kit kit = Tulip.getInstance().getKitRepository().getKit(kitName);

        if (kit == null) {
            player.sendMessage("Kit not found.");
            return;
        }

        FFARepository ffaRepository = Tulip.getInstance().getFfaRepository();
        ffaRepository.getMatches().stream()
                .filter(match -> match.getKit().equals(kit))
                .filter(match -> match.getPlayers().size() < match.getMaxPlayers())
                .findFirst()
                .ifPresent(match -> match.join(player));

    }
}

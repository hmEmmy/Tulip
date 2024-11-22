package me.emmy.tulip.util;

import lombok.experimental.UtilityClass;
import me.emmy.tulip.arena.command.ArenaCommand;
import me.emmy.tulip.arena.command.impl.*;
import me.emmy.tulip.command.admin.GamemodeCommand;
import me.emmy.tulip.command.admin.ReloadCommand;
import me.emmy.tulip.command.global.TulipCommand;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.ffa.command.admin.FFACommand;
import me.emmy.tulip.ffa.command.admin.impl.*;
import me.emmy.tulip.ffa.command.player.JoinCommand;
import me.emmy.tulip.ffa.command.player.LeaveCommand;
import me.emmy.tulip.ffa.command.player.PlayCommand;
import me.emmy.tulip.hotbar.command.HotbarItemsCommand;
import me.emmy.tulip.hotbar.command.RemoveHotbarItemsCommand;
import me.emmy.tulip.kit.command.KitCommand;
import me.emmy.tulip.kit.command.impl.*;
import me.emmy.tulip.profile.coins.command.CoinsCommand;
import me.emmy.tulip.profile.coins.command.impl.CoinsDonateCommand;
import me.emmy.tulip.profile.coins.command.impl.CoinsRequestCommand;
import me.emmy.tulip.profile.coins.command.impl.CoinsSetCommand;
import me.emmy.tulip.profile.kitlayout.command.KitLayoutCommand;
import me.emmy.tulip.profile.settings.command.SettingsCommand;
import me.emmy.tulip.profile.settings.command.toggle.ToggleScoreboardCommand;
import me.emmy.tulip.profile.settings.command.toggle.ToggleTablistCommand;
import me.emmy.tulip.profile.stats.command.StatsCommand;
import me.emmy.tulip.profile.stats.command.admin.ResetStatsCommand;
import me.emmy.tulip.profile.stats.command.admin.add.AddDeathsCommand;
import me.emmy.tulip.profile.stats.command.admin.add.AddKillsCommand;
import me.emmy.tulip.profile.stats.command.admin.set.SetDeathsCommand;
import me.emmy.tulip.profile.stats.command.admin.set.SetKillsCommand;
import me.emmy.tulip.shop.command.ShopCommand;
import me.emmy.tulip.spawn.command.SetSpawnCommand;
import me.emmy.tulip.spawn.command.SpawnCommand;

/**
 * @author Emmy
 * @project Tulip
 * @date 28/09/2024 - 09:05
 */
@UtilityClass
public class CommandUtility {
    /**
     * Register all commands based on their category.
     */
    public void registerCommands() {
        registerArenaCommands();
        registerAdminCommands();
        registerGlobalCommands();
        registerFFACommands();
        registerHotbarCommands();
        registerKitCommands();
        registerProfileCommands();
        registerOtherCommands();
        registerCoinCommands();
    }

    /**
     * Register the arena command and its subcommands.
     */
    private void registerArenaCommands() {
        new ArenaCommand();

        new ArenaCreateCommand();
        new ArenaDeleteCommand();
        new ArenaInfoCommand();
        new ArenaListCommand();
        new ArenaSetCenterCommand();
        new ArenaSetSafePosCommand();
        new ArenaSetSpawnCommand();
        new ArenaTeleportCommand();
    }

    /**
     * Register the admin commands.
     */
    private void registerAdminCommands() {
        new GamemodeCommand();
        new ReloadCommand();
    }

    /**
     * Register the global commands.
     */
    private void registerGlobalCommands() {
        new TulipCommand();
    }

    /**
     * Register the FFA command and its subcommands.
     */
    private void registerFFACommands() {
        //admin
        new FFACommand();

        new FFACreateCommand();
        new FFADeleteCommand();
        new FFAKickCommand();
        new FFAListCommand();
        new FFAListPlayersCommand();
        new FFASetMaxPlayersCommand();

        if (ConfigHandler.getInstance().getSettingsConfig().getBoolean("commands.ffa-join")) {
            new JoinCommand();
        }
        new LeaveCommand();
        new PlayCommand();
    }

    /**
     * Register the hotbar commands.
     */
    private void registerHotbarCommands() {
        new HotbarItemsCommand();
        new RemoveHotbarItemsCommand();
    }

    /**
     * Register the kit command and its subcommands.
     */
    private void registerKitCommands() {
        new KitCommand();

        new KitCreateCommand();
        new KitDeleteCommand();
        new KitGetInvCommand();
        new KitInfoCommand();
        new KitListCommand();
        new KitSetDescriptionCommand();
        new KitSetIconCommand();
        new KitSetInvCommand();
        new KitToggleCommand();
    }

    /**
     * Register the profile commands.
     */
    private void registerProfileCommands() {
        //admin
        new AddDeathsCommand();
        new AddKillsCommand();
        new SetDeathsCommand();
        new SetKillsCommand();
        new ResetStatsCommand();

        //player
        new KitLayoutCommand();
        new ToggleScoreboardCommand();
        new ToggleTablistCommand();
        new SettingsCommand();
        new StatsCommand();
    }

    /**
     * Register the other commands with no specific category.
     */
    private void registerOtherCommands() {
        new ShopCommand();
        new SpawnCommand();
        new SetSpawnCommand();
    }

    private void registerCoinCommands() {
        new CoinsCommand();
        new CoinsSetCommand();
        new CoinsRequestCommand();
        new CoinsDonateCommand();
    }
}
package me.emmy.tulip;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.arena.ArenaRepository;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.game.GameRepository;
import me.emmy.tulip.kit.KitRepository;
import me.emmy.tulip.utils.command.CommandFramework;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 21:12
 */
@Getter
@Setter
public class Tulip extends JavaPlugin {

    @Getter
    private static Tulip instance;

    private ConfigHandler configHandler;
    private CommandFramework commandFramework;
    private ArenaRepository arenaRepository;
    private KitRepository kitRepository;
    private GameRepository gameRepository;

    @Override
    public void onEnable() {
        instance = this;

        configHandler = new ConfigHandler();

        commandFramework = new CommandFramework();
        commandFramework.registerCommandsInPackage("me.emmy.tulip");

        arenaRepository = new ArenaRepository();
        arenaRepository.loadArenas();

        kitRepository = new KitRepository();
        kitRepository.loadKits();

        gameRepository = new GameRepository();
    }

    @Override
    public void onDisable() {
        arenaRepository.saveArenas();
        kitRepository.saveKits();
    }
}

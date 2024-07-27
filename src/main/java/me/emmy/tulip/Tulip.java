package me.emmy.tulip;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.arena.ArenaRepository;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.database.MongoService;
import me.emmy.tulip.game.GameRepository;
import me.emmy.tulip.kit.KitRepository;
import me.emmy.tulip.profile.ProfileRepository;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.ServerUtils;
import me.emmy.tulip.utils.command.CommandFramework;
import me.emmy.tulip.visual.ScoreboardVisualizer;
import me.emmy.tulip.visual.assemble.Assemble;
import me.emmy.tulip.visual.assemble.AssembleStyle;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

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
    private MongoService mongoService;
    private ProfileRepository profileRepository;

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
        gameRepository.loadGames();

        mongoService = new MongoService();
        mongoService.startMongo();

        profileRepository = new ProfileRepository();
        profileRepository.initializeEveryProfile();

        loadScoreboard();
        sendStartupMessage();
    }

    @Override
    public void onDisable() {
        ServerUtils.disconnectPlayers();

        arenaRepository.saveArenas();
        kitRepository.saveKits();
        gameRepository.saveGames();

        ServerUtils.stopTasks();

        sendShutdownMessage();
    }

    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardVisualizer());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    private void sendStartupMessage() {
        List<String> message = Arrays.asList(
                "",
                "&7&m-----------------------------------------------------",
                "&e&lTulip &7- &f" + getDescription().getDescription(),
                "&e-> Version: &f" + getDescription().getVersion(),
                "&e-> Author: &f" + getDescription().getAuthors().get(0),
                "&7&m-----------------------------------------------------",
                ""
        );
        message.forEach(line -> getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }

    private void sendShutdownMessage() {
        List<String> message = Arrays.asList(
                "",
                "&7&m-----------------------------------------------------",
                "&c&lDisabled Tulip...",
                "&7&m-----------------------------------------------------",
                ""
        );
        message.forEach(line -> getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }
}

package me.emmy.tulip;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.api.command.CommandFramework;
import me.emmy.tulip.arena.ArenaRepository;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.cooldown.CooldownRepository;
import me.emmy.tulip.database.MongoService;
import me.emmy.tulip.essential.command.admin.GamemodeCommand;
import me.emmy.tulip.ffa.FFARepository;
import me.emmy.tulip.ffa.safezone.FFASpawnHandler;
import me.emmy.tulip.ffa.safezone.task.FFASpawnTask;
import me.emmy.tulip.kit.KitRepository;
import me.emmy.tulip.profile.ProfileRepository;
import me.emmy.tulip.spawn.SpawnHandler;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.ServerUtils;
import me.emmy.tulip.visual.ScoreboardVisualizer;
import me.emmy.tulip.visual.assemble.Assemble;
import me.emmy.tulip.visual.assemble.AssembleStyle;
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
    private SpawnHandler spawnHandler;
    private ArenaRepository arenaRepository;
    private KitRepository kitRepository;
    private MongoService mongoService;
    private ProfileRepository profileRepository;
    private CooldownRepository cooldownRepository;
    private FFARepository ffaRepository;
    private FFASpawnHandler ffaSpawnHandler;

    @Override
    public void onEnable() {
        instance = this;

        configHandler = new ConfigHandler();

        commandFramework = new CommandFramework();
        commandFramework.registerCommandsInPackage("me.emmy.tulip");
        commandFramework.registerCommands(new GamemodeCommand());

        spawnHandler = new SpawnHandler();
        spawnHandler.loadSpawn();

        arenaRepository = new ArenaRepository();
        arenaRepository.loadArenas();

        kitRepository = new KitRepository();
        kitRepository.loadKits();

        ffaRepository = new FFARepository();
        ffaRepository.loadFFAMatches();

        ffaSpawnHandler = new FFASpawnHandler();
        ffaSpawnHandler.loadFFASpawn();

        mongoService = new MongoService();
        mongoService.startMongo();

        profileRepository = new ProfileRepository();
        profileRepository.initializeEveryProfile();

        cooldownRepository = new CooldownRepository();

        ServerUtils.registerListenersInPackage("me.emmy.tulip");
        ServerUtils.setupWorld();

        runTasks();
        loadScoreboard();
        CC.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        profileRepository.getProfiles().forEach((uuid, profile) -> profile.setOnline(false));
        profileRepository.getProfiles().forEach((uuid, profile) -> profile.saveProfile());

        ServerUtils.disconnectPlayers();

        arenaRepository.saveArenas();
        kitRepository.saveKits();
        ffaRepository.saveFFAMatches();

        ServerUtils.stopTasks();

        CC.sendShutdownMessage();
    }

    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardVisualizer());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    private void runTasks() {
        new FFASpawnTask(this.ffaSpawnHandler.getCuboid(), this).runTaskTimer(this, 0, 20);
        //new ArrowCleanupTask().runTaskTimer(this, 0, 6000);
    }
}
package me.emmy.tulip;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.api.assemble.Assemble;
import me.emmy.tulip.api.assemble.AssembleStyle;
import me.emmy.tulip.api.command.CommandFramework;
import me.emmy.tulip.arena.ArenaRepository;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.cooldown.CooldownRepository;
import me.emmy.tulip.database.MongoService;
import me.emmy.tulip.ffa.FFARepository;
import me.emmy.tulip.ffa.safezone.FFASpawnHandler;
import me.emmy.tulip.ffa.safezone.task.FFASpawnTask;
import me.emmy.tulip.kit.KitRepository;
import me.emmy.tulip.product.ProductRepository;
import me.emmy.tulip.profile.ProfileRepository;
import me.emmy.tulip.spawn.SpawnHandler;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.CommandUtility;
import me.emmy.tulip.util.ServerUtils;
import me.emmy.tulip.visual.scoreboard.ScoreboardVisualizer;
import me.emmy.tulip.visual.tablist.task.TablistUpdateTask;
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
    private ProductRepository productRepository;

    @Override
    public void onEnable() {
        instance = this;

        configHandler = new ConfigHandler();

        commandFramework = new CommandFramework();
        //commandFramework.registerCommandsInPackage("me.emmy.tulip");
        CommandUtility.registerCommands();

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

        productRepository = new ProductRepository();
        productRepository.loadProducts();

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

    /**
     * Load the scoreboard
     */
    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardVisualizer());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    /**
     * Run the tasks
     */
    private void runTasks() {
        new FFASpawnTask(this.ffaSpawnHandler.getCuboid(), this).runTaskTimer(this, 0, 20);

        if (configHandler.getTablistConfig().getBoolean("tablist.enabled")) {
            new TablistUpdateTask().runTaskTimer(this, 0L, 20L);
        }
    }
}

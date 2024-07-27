package me.emmy.tulip.game;

import lombok.Getter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.game.impl.MultipleKitsGameImpl;
import me.emmy.tulip.game.impl.SingleKitGameImpl;
import me.emmy.tulip.kit.Kit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 12:54
 */
@Getter
public class GameRepository {
    private final List<Game> games = new ArrayList<>();

    public void loadGames() {
        FileConfiguration config = ConfigHandler.getInstance().getGamesConfig();
        
        ConfigurationSection section = config.getConfigurationSection("games");
        if (section == null) {
            return;
        }
        
        for (String gameName : section.getKeys(false)) {
            String arenaName = config.getString(gameName + ".arena");
            String kitName = config.getString(gameName + ".kit");
            boolean allowMultipleKits = config.getBoolean(gameName + ".allowMultipleKits");

            Arena arena = Tulip.getInstance().getArenaRepository().getArena(arenaName);
            Kit kit = Tulip.getInstance().getKitRepository().getKit(kitName);

            Game game;
            if (allowMultipleKits) {
                game = new MultipleKitsGameImpl(arena, true);
            } else {
                game = new SingleKitGameImpl(kit, arena, false);
            }

            games.add(game);
        }
    }

    public void saveGames() {
        FileConfiguration config = ConfigHandler.getInstance().getGamesConfig();

        for (Game game : games) {
            String gameName = game.getName();
            String arenaName = game.getArena().getName();
            String kitName = game.getKit().getName();
            boolean allowMultipleKits = game.isAllowMultipleKits();

            config.set("games." + gameName + ".arena", arenaName);
            config.set("games." + gameName + ".kit", kitName);
            config.set("games." + gameName + ".allowMultipleKits", allowMultipleKits);
        }

        ConfigHandler.getInstance().saveConfig(ConfigHandler.getInstance().getConfigFile("storage/games.yml"), config);
    }

    public void saveGame(Game game) {
        FileConfiguration config = ConfigHandler.getInstance().getGamesConfig();

        String gameName = game.getName();
        if (gameName == null) {
            throw new IllegalArgumentException("Game name cannot be null");
        }

        String arenaName = (game.getArena() != null) ? game.getArena().getName() : "undefined";
        String kitName = (game.getKit() != null) ? game.getKit().getName() : "none";
        boolean allowMultipleKits = game.isAllowMultipleKits();

        config.set("games." + gameName + ".arena", arenaName);
        config.set("games." + gameName + ".kit", kitName);
        config.set("games." + gameName + ".allowMultipleKits", allowMultipleKits);

        ConfigHandler.getInstance().saveConfig(ConfigHandler.getInstance().getConfigFile("storage/games.yml"), config);
    }

    public void createGame(String gameName, Arena arena, Kit kit, boolean allowMultipleKits) {
        if (games.stream().anyMatch(game -> game.getName().equalsIgnoreCase(gameName))) {
            throw new IllegalArgumentException("A game with the name '" + gameName + "' already exists.");
        }

        Game game;
        if (allowMultipleKits) {
            game = new MultipleKitsGameImpl(arena, true);
        } else {
            game = new SingleKitGameImpl(kit, arena, false);
        }

        game.setName(gameName);

        games.add(game);
        saveGame(game);
    }
}

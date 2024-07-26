package me.emmy.tulip.arena;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 21:35
 */
@Getter
@Setter
public class ArenaRepository {

    private List<Arena> arenas = new ArrayList<>();

    /**
     * Load arenas from the arenas.yml file
     */
    public void loadArenas() {
        FileConfiguration config = ConfigHandler.getInstance().getArenasConfig();

        ConfigurationSection arenasConfig = config.getConfigurationSection("arenas");
        if (arenasConfig == null) {
            return;
        }

        for (String name : arenasConfig.getKeys(false)) {
            String key = "arenas." + name;

            List<String> debugMessage = Arrays.asList(
                    "",
                    "&aLoading arena: &e" + name,
                    "&aMax Players: &e" + config.getInt(key + ".maxPlayers"),
                    "&aMin Players: &e" + config.getInt(key + ".minPlayers"),
                    "&aSpawn: &e" + config.getString(key + ".spawn"),
                    "&aCenter: &e" + config.getString(key + ".center"),
                    ""
            );

            debugMessage.forEach(msg -> Bukkit.getConsoleSender().sendMessage(CC.translate(msg)));

            Arena arena = new Arena(
                    name,
                    config.getInt(key + ".maxPlayers"),
                    config.getInt(key + ".minPlayers"),
                    LocationUtil.deserialize(config.getString(key + ".spawn")),
                    LocationUtil.deserialize(config.getString(key + ".center"))
            );
            Bukkit.getConsoleSender().sendMessage(CC.translate("&aLoaded arena &e" + arena.getName()));
            arenas.add(arena);
        }
    }

    /**
     * Save all arenas to the arenas.yml file
     */
    public void saveArenas() {
        FileConfiguration config = ConfigHandler.getInstance().getArenasConfig();

        for (Arena arena : arenas) {
            String key = "arenas." + arena.getName();
            config.set(key + ".maxPlayers", arena.getMaxPlayers());
            config.set(key + ".minPlayers", arena.getMinPlayers());
            config.set(key + ".spawn", LocationUtil.serialize(arena.getSpawn()));
            config.set(key + ".center", LocationUtil.serialize(arena.getCenter()));
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate("&aSaved: &e" + arenas.size() + " arenas"));
        ConfigHandler.getInstance().saveConfig(ConfigHandler.getInstance().getConfigFile("storage/arenas.yml"), config);
    }

    /**
     * Save a specific arena to the arenas.yml file
     *
     * @param arena The arena to save
     */
    public void saveArena(Arena arena) {
        FileConfiguration config = ConfigHandler.getInstance().getArenasConfig();

        String key = "arenas." + arena.getName();
        //config.set(key + ".name", arena.etName());
        config.set(key + ".maxPlayers", arena.getMaxPlayers());
        config.set(key + ".minPlayers", arena.getMinPlayers());
        config.set(key + ".spawn", LocationUtil.serialize(arena.getSpawn()));
        config.set(key + ".center", LocationUtil.serialize(arena.getCenter()));

        ConfigHandler.getInstance().saveConfig(ConfigHandler.getInstance().getConfigFile("storage/arenas.yml"), config);
    }

    public Arena getArena(String name) {
        return arenas.stream()
                .filter(arena -> arena.getName().equalsIgnoreCase(name))
                .findFirst().
                orElse(null);
    }

    public void createArena(String name, int maxPlayers, int minPlayers, String spawn, String center) {
        Arena arena = new Arena(name, maxPlayers, minPlayers, LocationUtil.deserialize(spawn), LocationUtil.deserialize(center));
        arenas.add(arena);
        saveArena(arena);
    }

    public void deleteArena(String name) {
        Arena arena = getArena(name);
        arenas.remove(arena);
        FileConfiguration config = ConfigHandler.getInstance().getArenasConfig();
        config.set("arenas." + name, null);
        ConfigHandler.getInstance().saveConfig(ConfigHandler.getInstance().getConfigFile("storage/arenas.yml"), config);
    }
}
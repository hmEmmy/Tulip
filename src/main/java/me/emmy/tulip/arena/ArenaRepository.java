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
                    "&aLoading arena: &d" + name,
                    "&aSpawn: &d" + config.getString(key + ".spawn"),
                    "&aCenter: &d" + config.getString(key + ".center"),
                    "&aSafe Pos 1: &d" + config.getString(key + ".safePos1"),
                    "&aSafe Pos 2: &d" + config.getString(key + ".safePos2"),
                    ""
            );

            //debugMessage.forEach(msg -> Bukkit.getConsoleSender().sendMessage(CC.translate(msg)));

            Arena arena = new Arena(
                    name,
                    LocationUtil.deserialize(config.getString(key + ".spawn")),
                    LocationUtil.deserialize(config.getString(key + ".center")),
                    LocationUtil.deserialize(config.getString(key + ".safePos1")),
                    LocationUtil.deserialize(config.getString(key + ".safePos2"))
            );
            //Bukkit.getConsoleSender().sendMessage(CC.translate("&aLoaded arena &d" + arena.getName()));
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
            config.set(key + ".spawn", LocationUtil.serialize(arena.getSpawn()));
            config.set(key + ".center", LocationUtil.serialize(arena.getCenter()));
            config.set(key + ".safePos1", LocationUtil.serialize(arena.getSafePos1()));
            config.set(key + ".safePos2", LocationUtil.serialize(arena.getSafePos2()));
        }

        //Bukkit.getConsoleSender().sendMessage(CC.translate("&aSaved: &d" + arenas.size() + " arenas"));
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
        config.set(key + ".spawn", LocationUtil.serialize(arena.getSpawn()));
        config.set(key + ".center", LocationUtil.serialize(arena.getCenter()));
        config.set(key + ".safePos1", LocationUtil.serialize(arena.getSafePos1()));
        config.set(key + ".safePos2", LocationUtil.serialize(arena.getSafePos2()));

        ConfigHandler.getInstance().saveConfig(ConfigHandler.getInstance().getConfigFile("storage/arenas.yml"), config);
    }

    public Arena getArena(String name) {
        return arenas.stream()
                .filter(arena -> arena.getName().equalsIgnoreCase(name))
                .findFirst().
                orElse(null);
    }

    public void createArena(String name, String spawn, String center, String safePos1, String safePos2) {
        Arena arena = new Arena(name, LocationUtil.deserialize(spawn), LocationUtil.deserialize(center), LocationUtil.deserialize(safePos1), LocationUtil.deserialize(safePos2));
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

package me.emmy.tulip.config;

import lombok.Getter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 21:28
 */
@Getter
public class ConfigHandler {

    @Getter private static ConfigHandler instance;

    private final Tulip plugin = Tulip.getInstance();

    private final Map<String, File> configFiles = new HashMap<>();
    private final Map<String, FileConfiguration> fileConfigurations = new HashMap<>();

    private final FileConfiguration settingsConfig;
    private final FileConfiguration localeConfig;
    private final FileConfiguration hotbarConfig;
    private final FileConfiguration arenasConfig;
    private final FileConfiguration kitsConfig;
    private final FileConfiguration ffaConfig;
    private final FileConfiguration gameMenuConfig;

    private final String[] configFileNames = {
            "settings.yml", "locale.yml", "hotbar.yml", "storage/arenas.yml", "storage/kits.yml", "storage/ffa.yml", "menus/game-menu.yml"
    };

    public ConfigHandler() {
        for (String fileName : configFileNames) {
            loadConfig(fileName);
        }

        instance = this;

        settingsConfig = getConfig("settings.yml");
        localeConfig = getConfig("locale.yml");
        hotbarConfig = getConfig("hotbar.yml");
        arenasConfig = getConfig("storage/arenas.yml");
        kitsConfig = getConfig("storage/kits.yml");
        ffaConfig = getConfig("storage/ffa.yml");
        gameMenuConfig = getConfig("menus/game-menu.yml");
    }

    private void loadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        configFiles.put(fileName, configFile);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        fileConfigurations.put(fileName, config);
    }

    public void reloadConfigs() {
        for (String fileName : configFileNames) {
            loadConfig(fileName);
        }
    }

    public void saveConfigs() {
        for (Map.Entry<String, FileConfiguration> entry : fileConfigurations.entrySet()) {
            String fileName = entry.getKey();
            FileConfiguration config = entry.getValue();
            File configFile = configFiles.get(fileName);
            saveConfig(configFile, config);
        }
    }

    public void saveConfig(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
            fileConfiguration.load(configFile);
        } catch (Exception e) {
            Logger.logError("Failed to save config file: " + configFile.getName());
        }
    }

    public FileConfiguration getConfig(String fileName) {
        return fileConfigurations.get(fileName);
    }

    public File getConfigFile(String fileName) {
        return configFiles.get(fileName);
    }
}

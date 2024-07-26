package me.emmy.tulip.kit;

import lombok.Getter;
import me.emmy.tulip.config.ConfigHandler;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 22:00
 */
@Getter
public class KitRepository {
    private final List<Kit> kits = new ArrayList<>();

    /**
     * Load kits from the kits.yml file
     */
    public void loadKits() {
        FileConfiguration config = ConfigHandler.getInstance().getKitsConfig();
        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection == null) {
            return;
        }

        for (String name : kitsSection.getKeys(false)) {
            String key = "kits." + name;
            ItemStack[] inventory = config.getList(key + ".items").toArray(new ItemStack[0]);
            ItemStack[] armor = config.getList(key + ".armor").toArray(new ItemStack[0]);
            Material icon = Material.matchMaterial(config.getString(key + ".icon"));
            int iconData = config.getInt(key + ".icondata");

            Kit kit = new Kit(
                    name,
                    config.getString(key + ".description"),
                    inventory,
                    armor,
                    icon,
                    iconData,
                    config.getBoolean(key + ".enabled")
            );

            kits.add(kit);
        }
    }

    /**
     * Save kits to the kits.yml file
     */
    public void saveKits() {
        FileConfiguration config = ConfigHandler.getInstance().getKitsConfig();

        kits.forEach(kit -> {
            String key = "kits." + kit.getName();
            config.set(key + ".description", kit.getDescription());
            config.set(key + ".items", kit.getItems());
            config.set(key + ".armor", kit.getArmor());
            config.set(key + ".icon", kit.getIcon().name());
            config.set(key + ".icondata", kit.getIconData());
            config.set(key + ".enabled", kit.isEnabled());
        });

        File file = ConfigHandler.getInstance().getConfigFile("storage/kits.yml");
        ConfigHandler.getInstance().saveConfig(file, config);
    }

    /**
     * Save a kit to the kits.yml file
     *
     * @param kit The kit to save
     */
    public void saveKit(Kit kit) {
        FileConfiguration config = ConfigHandler.getInstance().getKitsConfig();
        String key = "kits." + kit.getName();
        config.set(key + ".description", kit.getDescription());
        config.set(key + ".items", kit.getItems());
        config.set(key + ".armor", kit.getArmor());
        config.set(key + ".icon", kit.getIcon().name());
        config.set(key + ".icondata", kit.getIconData());
        config.set(key + ".enabled", kit.isEnabled());

        File file = ConfigHandler.getInstance().getConfigFile("storage/kits.yml");
        ConfigHandler.getInstance().saveConfig(file, config);
    }

    /**
     * Delete a kit from the kits.yml file
     *
     * @param kit The kit to delete
     */
    public void deleteKit(Kit kit) {
        FileConfiguration config = ConfigHandler.getInstance().getConfig("storage/kits.yml");

        kits.remove(kit);
        config.set("kits." + kit.getName(), null);

        File file = ConfigHandler.getInstance().getConfigFile("storage/kits.yml");
        ConfigHandler.getInstance().saveConfig(file, config);
    }

    /**
     * Get a kit by name
     *
     * @param name The name of the kit
     * @return The kit
     */
    public Kit getKit(String name) {
        return kits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}

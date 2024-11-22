package me.emmy.tulip.spawn;

import lombok.Getter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 29/07/2024 - 23:02
 */
@Getter
public class SpawnHandler {

    private Location location;

    public void loadSpawn() {
        FileConfiguration config = ConfigHandler.getInstance().getSettingsConfig();

        String world = config.getString("spawn.world");
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        this.location = new Location(Tulip.getInstance().getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    public void setSpawn(Location location) {
        FileConfiguration config = ConfigHandler.getInstance().getSettingsConfig();

        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());

        ConfigHandler.getInstance().saveConfig(ConfigHandler.getInstance().getConfigFile("settings.yml"), config);
        this.location = location;
    }

    public void teleportToSpawn(Player player) {
        player.teleport(location);
    }
}

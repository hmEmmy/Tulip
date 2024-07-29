package me.emmy.tulip.ffa.safezone;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.ffa.safezone.cuboid.Cuboid;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.LocationUtil;
import me.emmy.tulip.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Tulip
 * @date 12/06/2024 - 22:14
 */
@Getter
@Setter
public class FFASpawnHandler {

    private final Tulip plugin = Tulip.getInstance();

    private Location spawnLocation;
    private Location safezoneMin;
    private Location safezoneMax;

    private Cuboid cuboid;

    public void loadFFASpawn() {
        FileConfiguration config = ConfigHandler.getInstance().getArenasConfig();
        Arena arena = Tulip.getInstance().getArenaRepository().getArenas().stream()
                .findFirst()
                .orElse(null);

        if (arena != null) {
            this.spawnLocation = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".spawn"));
            this.safezoneMin = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".safePos1"));
            this.safezoneMax = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".safePos2"));
        } else {
            Logger.logError("No ffa arenas found!");
            return;
        }

        if (this.safezoneMin != null && this.safezoneMax != null) {
            Bukkit.broadcastMessage(CC.translate("&a [+] FFA safezone found!"));
            Bukkit.broadcastMessage(CC.translate("&a [+] FFA safezone min: " + this.safezoneMin.toString()));
            Bukkit.broadcastMessage(CC.translate("&a [+] FFA safezone max: " + this.safezoneMax.toString()));
            this.cuboid = new Cuboid(safezoneMin, safezoneMax);
        } else {
            Logger.logError("No safezone found!");
        }
    }
}

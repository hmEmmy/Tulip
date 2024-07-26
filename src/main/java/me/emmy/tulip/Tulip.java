package me.emmy.tulip;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.arena.ArenaRepository;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 21:12
 */
@Getter
@Setter
public class Tulip extends JavaPlugin {

    private ArenaRepository arenaRepository;

    @Getter
    private static Tulip instance;

    @Override
    public void onEnable() {
        instance = this;

        arenaRepository = new ArenaRepository();

    }

    @Override
    public void onDisable() {

    }
}

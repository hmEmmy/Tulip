package me.emmy.tulip;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.arena.ArenaRepository;
import me.emmy.tulip.utils.command.CommandFramework;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 21:12
 */
@Getter
@Setter
public class Tulip extends JavaPlugin {

    private CommandFramework commandFramework;
    private ArenaRepository arenaRepository;

    @Getter
    private static Tulip instance;

    @Override
    public void onEnable() {
        instance = this;

        commandFramework = new CommandFramework();
        arenaRepository = new ArenaRepository();

    }

    @Override
    public void onDisable() {

    }
}

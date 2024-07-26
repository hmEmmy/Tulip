package me.emmy.tulip;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.arena.ArenaRepository;
import me.emmy.tulip.kit.KitRepository;
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

    @Getter
    private static Tulip instance;

    private CommandFramework commandFramework;
    private ArenaRepository arenaRepository;
    private KitRepository kitRepository;

    @Override
    public void onEnable() {
        instance = this;

        commandFramework = new CommandFramework();
        commandFramework.registerCommandsInPackage("me.emmy.tulip");

        arenaRepository = new ArenaRepository();
        kitRepository = new KitRepository();
    }

    @Override
    public void onDisable() {

    }
}

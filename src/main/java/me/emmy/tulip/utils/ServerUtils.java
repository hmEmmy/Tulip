package me.emmy.tulip.utils;

import lombok.experimental.UtilityClass;
import me.emmy.tulip.Tulip;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.util.Set;
import java.util.logging.Level;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 15:06
 */
@UtilityClass
public class ServerUtils {
    /**
     * Shutdown the server.
     */
    public void shutDown() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&cServer is shutting down..."));
        System.exit(0);
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(CC.prefix + "&fDisabling the plugin..."));
        Bukkit.getPluginManager().disablePlugin(Tulip.getInstance());
    }

    /**
     * Disconnect all players.
     */
    public void disconnectPlayers() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&cDisconnecting players..."));
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(CC.translate("&cServer is restarting...")));
    }

    /**
     * Stop all tasks.
     */
    public void stopTasks() {
        if (Bukkit.getScheduler().getActiveWorkers().isEmpty()) {
            return;
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate("&cStopping tasks..."));
        Bukkit.getScheduler().cancelTasks(Tulip.getInstance());
    }

    /**
     * Disable a specific plugin.
     *
     * @param pluginName the plugin name
     */
    public void disableSpecificPlugin(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin == null) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cAttempted to disable the plugin " + pluginName + " but it was not found."));
            return;
        }

        Bukkit.getConsoleSender().sendMessage(CC.translate("&cDisabling " + plugin.getName() + "..."));
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    /**
     * Register listeners in a specific package.
     *
     * @param packageName the package name
     */
    public void registerListenersInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Listener>> classes = reflections.getSubTypesOf(Listener.class);
        String[] excludedPackages = { "me.emmy.tulip.utils", "me.emmy.tulip.visual.assemble" };

        for (Class<? extends Listener> clazz : classes) {
            boolean isExcluded = false;
            for (String excludedPackage : excludedPackages) {
                if (clazz.getName().startsWith(excludedPackage)) {
                    isExcluded = true;
                    break;
                }
            }

            if (!isExcluded) {
                try {
                    Listener listener = clazz.getDeclaredConstructor().newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, Tulip.getInstance());
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&cRegistered listener: &f" + clazz.getSimpleName()));
                } catch (Exception e) {
                    //e.printStackTrace();
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to register listener: " + clazz.getSimpleName(), e);
                }
            }
        }
    }

    /**
     * Set up the world, clear entities, and set the time to day and disable weather.
     */
    public void setupWorld() {
        for (World world : Bukkit.getWorlds()) {
            if (world.isThundering() || world.hasStorm()) {
                world.setThundering(false);
                world.setStorm(false);
                world.setDifficulty(Difficulty.EASY);
            }

            world.setTime(6000L);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doWeatherCycle", "false");

            world.getEntities().forEach(entity -> {
                if (entity.getType().name().equals("DROPPED_ITEM")) {
                    entity.remove();
                }
            });
        }
    }
}

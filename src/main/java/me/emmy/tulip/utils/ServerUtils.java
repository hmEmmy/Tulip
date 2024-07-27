package me.emmy.tulip.utils;

import lombok.experimental.UtilityClass;
import me.emmy.tulip.Tulip;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

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
}

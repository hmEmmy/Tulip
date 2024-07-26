package me.emmy.tulip.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 21:30
 */
@UtilityClass
public class Logger {

    public void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(CC.errorPrefix + message));
    }
}

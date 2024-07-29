package me.emmy.tulip.utils;

import lombok.experimental.UtilityClass;
import me.emmy.tulip.Tulip;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 21:30
 */
@UtilityClass
public class CC {
    public String prefix = translate("&8[&dTulip&8] &7");
    public String errorPrefix = translate("&4[&cError&4] &7");

    /**
     * Translate a string with color codes
     *
     * @param string the string to translate
     * @return the translated string
     */
    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Send the startup message to the console
     */
    public void sendStartupMessage() {
        List<String> message = Arrays.asList(
                "",
                "&7&m-----------------------------------------------------",
                "&e&lTulip &7- &f" + Tulip.getInstance().getDescription().getDescription(),
                "&e-> Version: &f" + Tulip.getInstance().getDescription().getVersion(),
                "&e-> Author: &f" + Tulip.getInstance().getDescription().getAuthors().get(0),
                "&7&m-----------------------------------------------------",
                ""
        );
        message.forEach(line -> Tulip.getInstance().getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }

    /**
     * Send the shutdown message to the console
     */
    public void sendShutdownMessage() {
        List<String> message = Arrays.asList(
                "",
                "&7&m-----------------------------------------------------",
                "&c&lDisabled Tulip...",
                "&7&m-----------------------------------------------------",
                ""
        );
        message.forEach(line -> Tulip.getInstance().getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }
}

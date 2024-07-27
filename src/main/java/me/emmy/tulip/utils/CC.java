package me.emmy.tulip.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

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
}

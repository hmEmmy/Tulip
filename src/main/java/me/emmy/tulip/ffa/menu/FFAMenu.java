package me.emmy.tulip.ffa.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.api.menu.button.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class FFAMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Select a Kit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

        for (AbstractFFAMatch match : Tulip.getInstance().getFfaRepository().getMatches()) {
            buttons.put(slot, new FFAButton(match));
            slot++;
        }

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}

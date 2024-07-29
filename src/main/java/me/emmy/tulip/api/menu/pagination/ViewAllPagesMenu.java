package me.emmy.tulip.api.menu.pagination;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.api.menu.button.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ViewAllPagesMenu extends Menu {

    @NonNull
    PaginatedMenu menu;

    @Override
    public String getTitle(Player player) {
        return "Jump to page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(menu));

        int index = 10;

        for (int i = 1; i <= menu.getPages(player); i++) {
            buttons.put(index++, new JumpToPageButton(i, menu, menu.getPage() == i));

            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

}

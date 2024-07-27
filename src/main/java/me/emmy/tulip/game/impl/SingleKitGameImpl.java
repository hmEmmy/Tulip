package me.emmy.tulip.game.impl;

import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.game.Game;
import me.emmy.tulip.kit.Kit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 13:02
 */
public class SingleKitGameImpl extends Game {

    public SingleKitGameImpl(Kit kit, Arena arena, boolean allowMultipleKits) {
        super(kit, arena, allowMultipleKits);
    }

    @Override
    public void join(Player player) {

    }

    @Override
    public void setupPlayer(Player player) {

    }

    @Override
    public void handleDeath(Player player, Player killer) {

    }

    @Override
    public void handleRespawn(Player player) {

    }

    @Override
    public void leave(Player player) {

    }
}


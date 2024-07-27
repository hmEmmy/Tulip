package me.emmy.tulip.game;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.arena.Arena;
import me.emmy.tulip.game.enums.GameState;
import me.emmy.tulip.kit.Kit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 12:46
 */
@Getter
@Setter
public abstract class Game {

    private GameState state = GameState.SPAWN;

    private String name;
    private Kit kit;
    private Arena arena;
    private boolean allowMultipleKits;

    public Game(Kit kit, Arena arena, boolean allowMultipleKits) {
        this.kit = kit;
        this.arena = arena;
        this.allowMultipleKits = allowMultipleKits;
    }

    public Game(Arena arena, boolean allowMultipleKits) {
        this.arena = arena;
        this.allowMultipleKits = allowMultipleKits;
    }

    public abstract void join(Player player);
    public abstract void setupPlayer(Player player);
    public abstract void handleDeath(Player player, Player killer);
    public abstract void handleRespawn(Player player);
    public abstract void leave(Player player);
}

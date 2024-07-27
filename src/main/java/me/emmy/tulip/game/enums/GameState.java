package me.emmy.tulip.game.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 12:43
 */
@Getter
public enum GameState {
    SPAWN("Spawn", "The player is in the safezone."),
    FIGHTING("Fighting", "The player is fighting outside safezone.")

    ;

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumFFAState enum.
     *
     * @param name The name of the state.
     * @param description The description of the state.
     */
    GameState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

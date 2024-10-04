package me.emmy.tulip.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

/**
 * @author Emmy
 * @project Tulip
 * @date 26/07/2024 - 21:35
 */
@Getter
@Setter
@AllArgsConstructor
public class Arena {
    private String name;
    private Location spawn;
    private Location center;
    private Location safePos1;
    private Location safePos2;
}
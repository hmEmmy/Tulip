package me.emmy.tulip.cooldown;

import lombok.Getter;
import me.emmy.tulip.utils.MutableTriple;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Tulip
 * @date 5/27/2024
 */
@Getter
public class CooldownRepository {

    private final List<MutableTriple<UUID, String, Cooldown>> cooldowns;

    public CooldownRepository() {
        this.cooldowns = new ArrayList<>();
    }

    public void addCooldown(UUID uuid, String key, Cooldown cooldown) {
        cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(key));
        cooldowns.add(new MutableTriple<>(uuid, key, cooldown));
    }

    public Cooldown getCooldown(UUID uuid, String key) {
        return cooldowns.stream()
                .filter(triple -> triple.getA().equals(uuid) && triple.getB().equals(key))
                .map(MutableTriple::getC)
                .findFirst()
                .orElse(null);
    }
}
package me.emmy.tulip.profile.stats;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.kit.Kit;

import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 31/07/2024 - 09:46
 */
@Getter
@Setter
public class ProfileStats {
    private Map<Kit, Integer> kitKills;
    private Map<Kit, Integer> kitDeaths;

    public ProfileStats() {
        this.kitKills = Maps.newHashMap();
        this.kitDeaths = Maps.newHashMap();
    }

    public void incrementKitKills(Kit kit) {
        this.kitKills.put(kit, this.kitKills.getOrDefault(kit, 0) + 1);
    }

    public void incrementKitDeaths(Kit kit) {
        this.kitDeaths.put(kit, this.kitDeaths.getOrDefault(kit, 0) + 1);
    }

    public int getKitKills(Kit kit) {
        return this.kitKills.getOrDefault(kit, 0);
    }

    public int getKitDeaths(Kit kit) {
        return this.kitDeaths.getOrDefault(kit, 0);
    }

    public int getTotalKills() {
        return this.kitKills.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getTotalDeaths() {
        return this.kitDeaths.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getKDR() {
        return getTotalDeaths() == 0 ? getTotalKills() : getTotalKills() / getTotalDeaths();
    }

    public void resetStats() {
        this.kitKills.clear();
        this.kitDeaths.clear();
    }

    public void setKitDeaths(Map<Kit, Integer> kitDeaths) {
        this.kitDeaths = kitDeaths;
    }

    public void setKitKills(Map<Kit, Integer> kitKills) {
        this.kitKills = kitKills;
    }

    public void setKitDeaths(Kit kit, int deaths) {
        this.kitDeaths.put(kit, deaths);
    }

    public void setKitKills(Kit kit, int kills) {
        this.kitKills.put(kit, kills);
    }
}
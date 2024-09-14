package me.emmy.tulip.profile.stats;

import com.google.common.collect.Maps;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.profile.Profile;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Tulip
 * @date 31/07/2024 - 09:46
 */
@Getter
@Setter
public class ProfileStats {
    @Setter
    private Map<Kit, Integer> kitKills;
    @Setter
    private Map<Kit, Integer> kitDeaths;
    private Map<Kit, Integer> highestKillstreak;

    public ProfileStats() {
        this.kitKills = Maps.newHashMap();
        this.kitDeaths = Maps.newHashMap();
        this.highestKillstreak = Maps.newHashMap();
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
        this.highestKillstreak.clear();
    }

    public void setKitDeaths(Kit kit, int deaths) {
        this.kitDeaths.put(kit, deaths);
    }

    public void setKitKills(Kit kit, int kills) {
        this.kitKills.put(kit, kills);
    }

    public void addKitDeaths(Kit kit, int deaths) {
        this.kitDeaths.put(kit, this.kitDeaths.getOrDefault(kit, 0) + deaths);
    }

    public void addKitKills(Kit kit, int kills) {
        this.kitKills.put(kit, this.kitKills.getOrDefault(kit, 0) + kills);
    }

    public void setHighestKillstreak(Kit kit, int killstreak) {
        this.highestKillstreak.put(kit, killstreak);
    }

    public int getHighestKillstreak(Kit kit) {
        return this.highestKillstreak.getOrDefault(kit, 0);
    }

    public void incrementKillstreak(Kit kit) {
        int current = this.highestKillstreak.getOrDefault(kit, 0);
        this.highestKillstreak.put(kit, current + 1);
    }

    public void resetStreakAndSaveIfPresent(Kit kit, int killstreak, UUID player) {
        int currentHighest = this.highestKillstreak.getOrDefault(kit, 0);
        if (killstreak > currentHighest) {
            this.highestKillstreak.put(kit, killstreak);

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player);
            Document profileDocument = Tulip.getInstance().getProfileRepository().getCollection()
                    .find(Filters.eq("uuid", profile.getUuid().toString()))
                    .first();

            if (profileDocument != null) {
                Document statsDocument = (Document) profileDocument.get("stats");
                if (statsDocument == null) {
                    statsDocument = new Document();
                }

                Map<String, Integer> highestKillstreaksMap = new HashMap<>();
                for (Map.Entry<Kit, Integer> entry : this.highestKillstreak.entrySet()) {
                    highestKillstreaksMap.put(entry.getKey().getName(), entry.getValue());
                }
                statsDocument.put("highestKillstreaks", highestKillstreaksMap);

                Tulip.getInstance().getProfileRepository().getCollection()
                        .updateOne(Filters.eq("uuid", profile.getUuid().toString()), new Document("$set", new Document("stats", statsDocument)));
            }
        }
    }
}
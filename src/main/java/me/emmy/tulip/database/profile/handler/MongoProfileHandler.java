package me.emmy.tulip.database.profile.handler;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.database.profile.IProfile;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.ffa.FFARepository;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.profile.Profile;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 18:25
 */
public class MongoProfileHandler implements IProfile {
    /**
     * Load a profile
     *
     * @param profile the profile to load
     */
    public void loadProfile(Profile profile) {
        Document document = Tulip.getInstance().getProfileRepository().getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            this.saveProfile(profile);
            return;
        }

        profile.setName(document.getString("name"));
        profile.setOnline(document.getBoolean("online"));

        Document statsDocument = (Document) document.get("stats");
        if (statsDocument != null) {
            Map<Kit, Integer> kitKills = new HashMap<>();
            Map<Kit, Integer> kitDeaths = new HashMap<>();

            Document killsDoc = (Document) statsDocument.get("kills");
            Document deathsDoc = (Document) statsDocument.get("deaths");

            if (killsDoc != null) {
                for (Map.Entry<String, Object> entry : killsDoc.entrySet()) {
                    Kit kit = Tulip.getInstance().getFfaRepository().getMatches().stream().map(AbstractFFAMatch::getKit).filter(k -> k.getName().equals(entry.getKey())).findFirst().orElse(null);
                            kitKills.put(kit, (Integer) entry.getValue());
                }
            }

            if (deathsDoc != null) {
                for (Map.Entry<String, Object> entry : deathsDoc.entrySet()) {
                    Kit kit = Tulip.getInstance().getFfaRepository().getMatches().stream().map(AbstractFFAMatch::getKit).filter(k -> k.getName().equals(entry.getKey())).findFirst().orElse(null);
                            kitDeaths.put(kit, (Integer) entry.getValue());
                }
            }

            profile.getStats().setKitKills(kitKills);
            profile.getStats().setKitDeaths(kitDeaths);
        }
    }

    /**
     * Save a profile
     *
     * @param profile the profile to save
     */
    public void saveProfile(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("name", profile.getName());
        document.put("online", profile.isOnline());

        Document statsDocument = new Document();

        FFARepository ffaRepository = Tulip.getInstance().getFfaRepository();
        Map<String, Integer> killsMap = new HashMap<>();
        Map<String, Integer> deathsMap = new HashMap<>();

        for (AbstractFFAMatch match : ffaRepository.getMatches()) {
            Kit kit = match.getKit();
            String kitName = kit.getName();

            Integer kills = profile.getStats().getKitKills().getOrDefault(kit, 0);
            killsMap.put(kitName, kills);

            Integer deaths = profile.getStats().getKitDeaths().getOrDefault(kit, 0);
            deathsMap.put(kitName, deaths);
        }

        statsDocument.put("kills", killsMap);
        statsDocument.put("deaths", deathsMap);

        document.put("stats", statsDocument);

        Tulip.getInstance().getProfileRepository().getCollection()
                .replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

}
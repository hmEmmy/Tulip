package me.emmy.tulip.database.profile.handler;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.database.profile.IProfile;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.ffa.FFARepository;
import me.emmy.tulip.kit.Kit;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.utils.DatabaseUtil;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
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

        Document settingsDocument = (Document) document.get("settings");
        profile.getSettings().setShowScoreboard(settingsDocument.getBoolean("showScoreboard"));

        Document statsDocument = (Document) document.get("stats");
        if (statsDocument != null) {
            Map<Kit, Integer> kitKills = new HashMap<>();
            Map<Kit, Integer> kitDeaths = new HashMap<>();

            Document killsDoc = (Document) statsDocument.get("kills");
            Document deathsDoc = (Document) statsDocument.get("deaths");

            if (killsDoc != null) {
                for (Map.Entry<String, Object> entry : killsDoc.entrySet()) {
                    Kit kit = Tulip.getInstance().getKitRepository().getKit(entry.getKey());
                    if (kit != null) {
                        kitKills.put(kit, (Integer) entry.getValue());
                    }
                }
            }

            if (deathsDoc != null) {
                for (Map.Entry<String, Object> entry : deathsDoc.entrySet()) {
                    Kit kit = Tulip.getInstance().getKitRepository().getKit(entry.getKey());
                    if (kit != null) {
                        kitDeaths.put(kit, (Integer) entry.getValue());
                    }
                }
            }

            profile.getStats().setKitKills(kitKills);
            profile.getStats().setKitDeaths(kitDeaths);
        }

        Document kitLayoutsDoc = (Document) document.get("kitLayouts");
        if (kitLayoutsDoc != null) {
            for (Kit kit : Tulip.getInstance().getKitRepository().getKits()) {
                String kitName = kit.getName();
                Document layoutDoc = (Document) kitLayoutsDoc.get(kitName);
                if (layoutDoc != null) {
                    ItemStack[] items = DatabaseUtil.deserializeItemStackArray(layoutDoc.getString("items"));
                    profile.getKitLayout().setLayout(kitName, items);
                }
            }
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

        Document settingsDocument = new Document();
        settingsDocument.put("showScoreboard", profile.getSettings().isShowScoreboard());
        document.put("settings", settingsDocument);

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

        Document kitLayoutsDoc = new Document();
        for (Kit kit : Tulip.getInstance().getKitRepository().getKits()) {
            String kitName = kit.getName();

            Document layoutDoc = new Document();
            layoutDoc.put("items", DatabaseUtil.serializeItemStackArray(profile.getKitLayout().getLayout(kitName)));

            kitLayoutsDoc.put(kitName, layoutDoc);
        }
        document.put("kitLayouts", kitLayoutsDoc);

        Tulip.getInstance().getProfileRepository().getCollection()
                .replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }
}

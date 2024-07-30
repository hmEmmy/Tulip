package me.emmy.tulip.database.profile.handler;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.database.profile.IProfile;
import me.emmy.tulip.profile.Profile;
import org.bson.Document;

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

        Document statsDocument = (Document) document.get("stats");

        profile.setName(document.getString("name"));
        profile.setOnline(document.getBoolean("online"));
        profile.setKills(statsDocument.getInteger("kills"));
        profile.setDeaths(statsDocument.getInteger("deaths"));
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
        statsDocument.append("kills", profile.getKills());
        statsDocument.append("deaths", profile.getDeaths());
        document.put("stats", statsDocument);

        Tulip.getInstance().getProfileRepository().getCollection().replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }
}

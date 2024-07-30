package me.emmy.tulip.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.profile.enums.EnumProfileState;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 18:26
 */
@Getter
@Setter
public class Profile {
    public MongoCollection<Document> collection;

    private String name;
    private UUID uuid;
    private boolean online;
    private AbstractFFAMatch ffaMatch;
    private EnumProfileState state;
    private int kills = 0;
    private int deaths = 0;

    /**
     * Constructor for the Profile class
     *
     * @param uuid the UUID of the profile
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.online = false;
    }

    /**
     * Load all the profile data
     */
    public void loadProfile() {
        Tulip.getInstance().getProfileRepository().getProfile().loadProfile(this);
    }

    /**
     * Save all the profile data
     */
    public void saveProfile() {
        Tulip.getInstance().getProfileRepository().getProfile().saveProfile(this);
    }

    /**
     * Increment the kills of the profile
     */
    public void incrementKills() {
        this.kills++;
    }

    /**
     * Increment the deaths of the profile
     */
    public void incrementDeaths() {
        this.deaths++;
    }
}

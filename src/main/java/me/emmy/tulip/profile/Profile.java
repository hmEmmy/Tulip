package me.emmy.tulip.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.Tulip;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * @author Remi
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

    /**
     * Constructor for the Profile class
     *
     * @param uuid the UUID of the profile
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(this.uuid).getName();
    }

    public void loadProfile() {
        Tulip.getInstance().getProfileRepository().getProfile().loadProfile(this);
    }

    public void saveProfile() {
        Tulip.getInstance().getProfileRepository().getProfile().saveProfile(this);
    }
}

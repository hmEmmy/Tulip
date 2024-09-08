package me.emmy.tulip.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.profile.coins.ProfileCoins;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.profile.kitlayout.ProfileKitLayout;
import me.emmy.tulip.profile.settings.ProfileSettings;
import me.emmy.tulip.profile.stats.ProfileStats;
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
    private ProfileStats stats;
    private ProfileSettings settings;
    private ProfileKitLayout kitLayout;
    private ProfileCoins coins;

    /**
     * Constructor for the Profile class
     *
     * @param uuid the UUID of the profile
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.online = false;
        this.stats = new ProfileStats();
        this.settings = new ProfileSettings();
        this.kitLayout = new ProfileKitLayout();
        this.coins = new ProfileCoins(0);
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
}

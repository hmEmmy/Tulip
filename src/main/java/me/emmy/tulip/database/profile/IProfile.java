package me.emmy.tulip.database.profile;

import me.emmy.tulip.profile.Profile;

/**
 * @author Emmy
 * @project Tulip
 * @date 6/23/2024
 */
public interface IProfile {

    /**
     * Load a profile
     *
     * @param profile the profile to load
     */
    void loadProfile(Profile profile);

    /**
     * Save a profile
     *
     * @param profile the profile to save
     */
    void saveProfile(Profile profile);
}

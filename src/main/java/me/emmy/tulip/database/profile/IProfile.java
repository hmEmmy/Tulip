package me.emmy.tulip.database.profile;

import me.emmy.tulip.profile.Profile;

/**
 * @author Remi
 * @project Tulip
 * @date 6/23/2024
 */
public interface IProfile {

    void loadProfile(Profile profile);

    void saveProfile(Profile profile);
}

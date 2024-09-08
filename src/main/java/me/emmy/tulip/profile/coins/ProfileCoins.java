package me.emmy.tulip.profile.coins;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project Tulip
 * @date 08/09/2024 - 15:06
 */
@Setter
@Getter
public class ProfileCoins {

    private int coins;

    /**
     * Instantiates a new Profile coins.
     *
     * @param coins the coins
     */
    public ProfileCoins(int coins) {
        this.coins = coins;
    }

    /**
     * Add coins.
     *
     * @param coins the coins
     */
    public void addCoins(int coins) {
        this.coins += coins;
    }

    /**
     * Remove coins.
     *
     * @param coins the coins
     */
    public void removeCoins(int coins) {
        this.coins -= coins;
    }
}

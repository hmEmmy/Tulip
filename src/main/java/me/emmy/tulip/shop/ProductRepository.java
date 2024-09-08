package me.emmy.tulip.shop;

import lombok.Getter;
import me.emmy.tulip.shop.enums.EnumProductType;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 /**
 * @author Emmy
 * @project Tulip
 * @date 08/09/2024 - 14:48
 */
@Getter
public class ProductRepository {

    private final Map<String, Product> products = new HashMap<>();

    public void loadProducts() {
        products.put("Strength", new Product("Strength", "&cStrength", "&7Gives you strength", Material.POTION, 100, 1, EnumProductType.EFFECT));
        products.put("Speed", new Product("Speed", "&bSpeed", "&7Gives you speed", Material.POTION, 100, 1, EnumProductType.EFFECT));

        products.put("CoinBoosterTier1", new Product("CoinBoosterTier1", "&6Coin Booster (Tier 1)", "&7Doubles your coins by one each time you kill a player", Material.GOLD_INGOT, 500, 1, EnumProductType.COIN_BOOSTER));
        products.put("CoinBoosterTier2", new Product("CoinBoosterTier2", "&6Coin Booster (Tier 2)", "&7Doubles your coins by two each time you kill a player", Material.GOLD_INGOT, 1000, 1, EnumProductType.COIN_BOOSTER));
    }
}

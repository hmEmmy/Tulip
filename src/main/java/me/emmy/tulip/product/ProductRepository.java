package me.emmy.tulip.product;

import lombok.Getter;
import me.emmy.tulip.product.enums.EnumProductType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 /**
 * @author Emmy
 * @project Tulip
 * @date 08/09/2024 - 14:48
 */
@Getter
public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

    /**
     * Load all products
     */
    public void loadProducts() {
        products.add(new Product("Strength", "&cStrength I", "&eGives you strength 1", Material.POTION, 100, 0, EnumProductType.EFFECT));
        products.add(new Product("Speed", "&bSpeed I", "&eGives you speed 1", Material.POTION, 100, 0, EnumProductType.EFFECT));
        products.add(new Product("CoinBoosterTier1", "&eCoin Booster &d(Tier 1)", "&7Doubles your coins by one each time you kill a player", Material.GOLD_INGOT, 500, 0, EnumProductType.COIN_BOOSTER));
        products.add(new Product("CoinBoosterTier2", "&eCoin Booster &d(Tier 2)", "&7Doubles your coins by two each time you kill a player", Material.GOLD_INGOT, 1000, 0, EnumProductType.COIN_BOOSTER));
    }

    /**
     * Get a product by its name
     *
     * @param name the name of the product
     * @return the product
     */
    public Product getProduct(String name) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }
}
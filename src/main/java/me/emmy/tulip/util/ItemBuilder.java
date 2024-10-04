package me.emmy.tulip.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder implements Listener {
	private final ItemStack itemStack;
	private String command;
	private boolean commandEnabled = true;

	public ItemBuilder(Material mat) {
		itemStack = new ItemStack(mat);
	}

	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemBuilder amount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder name(String name) {
		ItemMeta meta = itemStack.getItemMeta();

		// Check if meta is null and create a new one if needed
		if (meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		}

		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		itemStack.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(String name) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.add(ChatColor.translateAlternateColorCodes('&', name));
		meta.setLore(lore);

		itemStack.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(String... lore) {
		List<String> toSet = new ArrayList<>();
		ItemMeta meta = itemStack.getItemMeta();

		for (String string : lore) {
			toSet.add(ChatColor.translateAlternateColorCodes('&', string));
		}

		meta.setLore(toSet);
		itemStack.setItemMeta(meta);

		return this;
	}

	public ItemBuilder lore(List<String> lore) {
		List<String> toSet = new ArrayList<>();
		ItemMeta meta = itemStack.getItemMeta();

		for (String string : lore) {
			toSet.add(ChatColor.translateAlternateColorCodes('&', string));
		}

		meta.setLore(toSet);
		itemStack.setItemMeta(meta);

		return this;
	}

	public ItemBuilder durability(int durability) {
		itemStack.setDurability((short) durability);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		itemStack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment) {
		itemStack.addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder type(Material material) {
		itemStack.setType(material);
		return this;
	}

	public ItemBuilder clearLore() {
		ItemMeta meta = itemStack.getItemMeta();

		meta.setLore(new ArrayList<>());
		itemStack.setItemMeta(meta);

		return this;
	}

	public ItemBuilder clearEnchantments() {
		for (Enchantment e : itemStack.getEnchantments().keySet()) {
			itemStack.removeEnchantment(e);
		}

		return this;
	}

	public ItemBuilder hideMeta() {
		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder command(String command) {
		this.command = command;
		return this;
	}

	public ItemBuilder commandEnabled(boolean enabled) {
		this.commandEnabled = enabled;
		return this;
	}

	public ItemStack build() {
		ItemMeta meta = itemStack.getItemMeta();

		if (commandEnabled && command != null) {
			List<String> lore = meta.getLore();

			if (lore == null) {
				lore = new ArrayList<>();
			}

			lore.add("command:" + command);
			meta.setLore(lore);
		}

		itemStack.setItemMeta(meta);
		return itemStack;
	}
}
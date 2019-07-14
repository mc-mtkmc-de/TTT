package de.ttt.utils;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	private ItemStack item;
	private ItemMeta itemMeta;
	
	public ItemBuilder(Material material) {
		item = new ItemStack(material);
		itemMeta = item.getItemMeta();
		
	}
	
	public ItemBuilder setDisplayName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}
	
	public ItemBuilder setLore(String... lore) {
		itemMeta.setLore(Arrays.asList(lore));
		return this;
	}
	
	public ItemStack build() {
		item.setItemMeta(itemMeta);
		return item;
	}
	
}

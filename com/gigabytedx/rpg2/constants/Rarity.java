package com.gigabytedx.rpg2.constants;

import org.bukkit.ChatColor;

public enum Rarity {
	EXOTIC("Exotic", ChatColor.GOLD + ""),
	LEGENDARY("Legendary", ChatColor.DARK_PURPLE + ""),
	RARE("Rare", ChatColor.BLUE + ""),
	UNCOMMON("Uncommon", ChatColor.GREEN + ""),
	COMMON("Common", ChatColor.WHITE + "");
	
	private final String rarityName;
	private final String rarityColor;
	
	
	
	private Rarity(String rarityName, String rarityColor) {
		this.rarityName = rarityName;
		this.rarityColor = rarityColor;
	}
	
	public String getRarityName() {
		return rarityName;
	}
	public String getRarityColor() {
		return rarityColor;
	}

	
}

package com.gigabytedx.rpg2.constants;

public enum ConfigPathNames {
	LIST_NAME("item pool"),
	ID("name"),
	DEBUG_ENABLED("show debug"),
	LOWER_PERCENTAGE_THRESHOLD("lower pecentage threshold"),
	UPPER_PERCENTAGE_THRESHOLD("upper pecentage threshold"),
	MAX_ITEM_LEVEL("max item level"),
	BASE_ITEM_LEVEL("base item level"),
	ITEM_COUNT("amount of items allowed"),
	BASE_DAMAGE_SCALING("scaling percent per level"),
	DISABLED_ENCHANTMENTS("disabled enchantments"),
	DROP_RATE("item drop rate"),
	RARITY_WEIGHTS("rareness weights");
	
	private final String pathName;
	
	ConfigPathNames(String pathName){
		this.pathName = pathName;
	}

	public String getName() {
		return pathName;
	}
	
}

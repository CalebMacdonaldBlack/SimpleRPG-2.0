package com.gigabytedx.rpg2.constants;

import org.bukkit.ChatColor;

import com.gigabytedx.rpg2.Main;

public enum NamesAndPrefixes {
	ITEM_LEVEL_PREFIX(ChatColor.AQUA + "Item Level: " + ChatColor.GOLD),
	DEBUG_PREFIX("[Debug]: "),
	PLUGIN_NAME(ChatColor.GOLD + "[" + ChatColor.BLUE + Main.PLUGIN_NAME + ChatColor.GOLD + "] " + ChatColor.GREEN),
	MOB_LEVEL_PREFIX(ChatColor.BLACK + "Level: " + ChatColor.GREEN),
	CLASS_NAME(ChatColor.AQUA + "Class: " + ChatColor.DARK_BLUE),
	CLASS_NAME_COLOR(ChatColor.BLACK + "");
	
	private final String name;

	private NamesAndPrefixes(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
}

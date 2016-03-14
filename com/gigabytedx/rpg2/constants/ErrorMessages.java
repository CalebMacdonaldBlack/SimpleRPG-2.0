package com.gigabytedx.rpg2.constants;

import org.bukkit.ChatColor;

public enum ErrorMessages {
	INVALID_NAME("Tried to calculate an invalid range. You will find that your itemConfig file has something wrong with it"),
	INVALID_ITEM_TYPE("Could not generate the item! An item type specified in the 'itemConfigFile.yml' is invalid and could not be found. Bukkit is weird sometimes and has funny item names. You can google them."),
	ITEM_ADD_FAILURE("No item was added to the inventory!"),
	INTERNAL_ERROR(NamesAndPrefixes.PLUGIN_NAME.getName() + ChatColor.RED + "An internal error has occurred! Please notify an administrator."),
	CONFIG_ERROR("Could not get valid data from the config file."),
	SENDER_ERROR("Only a player can use this command."),
	COMMAND_ARGUMENT_ERROR(NamesAndPrefixes.PLUGIN_NAME.getName() + ChatColor.RED + "Invalid Arguments: "),
	NO_REGION_ERROR(NamesAndPrefixes.PLUGIN_NAME.getName() + ChatColor.RED + "There is no region set where you are standing!"),
	LEVEL_IN_NAME_MISSING("Could not find a level in a mobs name!"),
	CHANGE_INVENTORY_IN_REGION(ChatColor.RED + "You cannot alter your inventory in a hostile area!"),
	RARITY_ERROR(NamesAndPrefixes.PLUGIN_NAME.getName() + ChatColor.RED +"Generated a common rarity as that rarity was not found: ");
	
	private final String errorMessage;
	
	ErrorMessages(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}

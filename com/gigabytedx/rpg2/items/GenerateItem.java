package com.gigabytedx.rpg2.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.Classes;
import com.gigabytedx.rpg2.constants.ConfigPathNames;
import com.gigabytedx.rpg2.constants.ErrorMessages;
import com.gigabytedx.rpg2.constants.NamesAndPrefixes;
import com.gigabytedx.rpg2.constants.Rarity;
import com.gigabytedx.rpg2.inventory.InventoryManagement;
import com.gigabytedx.rpg2.tools.ToolsRandom;
import com.rit.sucy.CustomEnchantment;

public class GenerateItem {
	// generated a random item with an item level between specified range
		public static ItemStack getNewItem(Object itemType, PlayerInventory playerInventory, String rarity,
				String restrictedClass, Main plugin) {
			ItemStack itemStack = null;
			ItemMeta itemMeta;
			int itemCount;
			double itemLevelBase;
			double itemLevelLowestPossible;
			double itemLevelHighestPossible;
			// get gear level range
			try {
				itemCount = plugin.getConfig().getInt(ConfigPathNames.ITEM_COUNT.getName());
				itemLevelBase = getGearLevel(playerInventory, plugin) / itemCount;
				itemLevelLowestPossible = itemLevelBase - (itemLevelBase
						* plugin.getConfig().getDouble(ConfigPathNames.LOWER_PERCENTAGE_THRESHOLD.getName()));
				itemLevelHighestPossible = itemLevelBase + (itemLevelBase
						* plugin.getConfig().getDouble(ConfigPathNames.UPPER_PERCENTAGE_THRESHOLD.getName()));
			} catch (Exception e) {
				plugin.logError(ErrorMessages.CONFIG_ERROR.getErrorMessage() + " Problem is with one of the following: "
						+ String.format("%s,%s,%s", ConfigPathNames.ITEM_COUNT.getName(),
								ConfigPathNames.LOWER_PERCENTAGE_THRESHOLD.getName(),
								ConfigPathNames.UPPER_PERCENTAGE_THRESHOLD.getName()));
				return null;
			}

			// check if the randomly selected material type is valid
			try {
				itemStack = new ItemStack(Material.valueOf((String) itemType));
				itemMeta = itemStack.getItemMeta();
			} catch (IllegalArgumentException e) {
				plugin.logError(ErrorMessages.INVALID_ITEM_TYPE.getErrorMessage());
				return null;
			}

			// generate an item level within relative range
			int chosenItemLevel = ToolsRandom.getRandomIndex((int) itemLevelLowestPossible, (int) itemLevelHighestPossible, plugin);
			if (chosenItemLevel > plugin.getConfig().getInt(ConfigPathNames.MAX_ITEM_LEVEL.getName())) {
				chosenItemLevel = plugin.getConfig().getInt(ConfigPathNames.MAX_ITEM_LEVEL.getName());
			}

			// set the display name of the item
			String list = ConfigPathNames.LIST_NAME.getName();
			String type = (String) itemType;
			String name = ConfigPathNames.ID.getName();
			String itemName = plugin.itemConfig.getString(String.format("%s.%s.%s", list, type, name));
			itemMeta.setDisplayName(itemName);

			// add level, rarity and class to lore
			List<String> lore = new ArrayList<>();
			lore.add(addRarityName(rarity));
			lore.add("");// new line
			lore.add(ChatColor.WHITE + String.format("%s%s", NamesAndPrefixes.CLASS_NAME.getName(), getClassName(restrictedClass)));
			getClassName(restrictedClass);
			lore.add(NamesAndPrefixes.ITEM_LEVEL_PREFIX.getName() + chosenItemLevel);
			itemMeta.setLore(lore);
			itemStack.setItemMeta(itemMeta);
			
			AddEnchantments.enchant(itemStack, rarity, plugin);
			return itemStack;

		}
		
		public static ItemStack getNewItem(Object itemType, int level, String rarity,
				String restrictedClass, Main plugin) {
			ItemStack itemStack = null;
			ItemMeta itemMeta;

			// check if the randomly selected material type is valid
			try {
				itemStack = new ItemStack(Material.valueOf((String) itemType));
				itemMeta = itemStack.getItemMeta();
			} catch (IllegalArgumentException e) {
				plugin.logError(ErrorMessages.INVALID_ITEM_TYPE.getErrorMessage());
				return null;
			}

			// set the display name of the item
			String list = ConfigPathNames.LIST_NAME.getName();
			String type = (String) itemType;
			String name = ConfigPathNames.ID.getName();
			String itemName = plugin.itemConfig.getString(String.format("%s.%s.%s", list, type, name));
			itemMeta.setDisplayName(itemName);

			// add level, rarity and class to lore
			List<String> lore = new ArrayList<>();
			lore.add(addRarityName(rarity));
			lore.add("");// new line
			lore.add(ChatColor.WHITE + String.format("%s%s", NamesAndPrefixes.CLASS_NAME.getName(), getClassName(restrictedClass)));
			getClassName(restrictedClass);
			lore.add(NamesAndPrefixes.ITEM_LEVEL_PREFIX.getName() + level);
			itemMeta.setLore(lore);
			itemStack.setItemMeta(itemMeta);
			
			AddEnchantments.enchant(itemStack, rarity, plugin);
			return itemStack;

		}
		
		// gets the sum of all item levels of the items in the players hotbar and
		// armor slots
		private static int getGearLevel(PlayerInventory playerInventory, Main plugin) {
			return InventoryManagement.getGearLevel(playerInventory, plugin)
					+ plugin.getConfig().getInt(ConfigPathNames.BASE_ITEM_LEVEL.getName());

		}

		private static String getClassName(String restrictedClass) {
			return Classes.valueOf(restrictedClass).getClassName();

		}
		
		private static String addRarityName(String rarity) {
			return Rarity.valueOf(rarity).getRarityColor() + Rarity.valueOf(rarity).getRarityName();
		}

		public static ItemStack getNewStaticItem(String itemType, String name, int level, String rarity, String restrictedClass,
				Main plugin, List<CustomEnchantment> enchants) {
			
				ItemStack itemStack = null;
				ItemMeta itemMeta;

				// check if the randomly selected material type is valid
				try {
					itemStack = new ItemStack(Material.valueOf((String) itemType));
					itemMeta = itemStack.getItemMeta();
				} catch (IllegalArgumentException e) {
					plugin.logError(ErrorMessages.INVALID_ITEM_TYPE.getErrorMessage());
					return null;
				}
				itemMeta.setDisplayName(name);

				// add level, rarity and class to lore
				List<String> lore = new ArrayList<>();
				lore.add(addRarityName(rarity));
				lore.add("");// new line
				lore.add(ChatColor.WHITE + String.format("%s%s", NamesAndPrefixes.CLASS_NAME.getName(), getClassName(restrictedClass)));
				getClassName(restrictedClass);
				lore.add(NamesAndPrefixes.ITEM_LEVEL_PREFIX.getName() + level);
				itemMeta.setLore(lore);
				itemStack.setItemMeta(itemMeta);
				
				for(CustomEnchantment enchant: enchants){
					enchant.addToItem(itemStack, 3);
				}
				return itemStack;

			}

		public static ItemStack getNewItemFromLevel(String itemType, String rarity, String restrictedClass, int level, Main plugin) {
			ItemStack itemStack = null;
			ItemMeta itemMeta;
			int itemCount;
			double itemLevelBase;
			double itemLevelLowestPossible;
			double itemLevelHighestPossible;
			// get gear level range
			try {
				itemCount = plugin.getConfig().getInt(ConfigPathNames.ITEM_COUNT.getName());
				itemLevelBase = (level*10) / itemCount;
				itemLevelLowestPossible = itemLevelBase - (itemLevelBase
						* plugin.getConfig().getDouble(ConfigPathNames.LOWER_PERCENTAGE_THRESHOLD.getName()));
				itemLevelHighestPossible = itemLevelBase + (itemLevelBase
						* plugin.getConfig().getDouble(ConfigPathNames.UPPER_PERCENTAGE_THRESHOLD.getName()));
			} catch (Exception e) {
				plugin.logError(ErrorMessages.CONFIG_ERROR.getErrorMessage() + " Problem is with one of the following: "
						+ String.format("%s,%s,%s", ConfigPathNames.ITEM_COUNT.getName(),
								ConfigPathNames.LOWER_PERCENTAGE_THRESHOLD.getName(),
								ConfigPathNames.UPPER_PERCENTAGE_THRESHOLD.getName()));
				return null;
			}

			// check if the randomly selected material type is valid
			try {
				itemStack = new ItemStack(Material.valueOf((String) itemType));
				itemMeta = itemStack.getItemMeta();
			} catch (IllegalArgumentException e) {
				plugin.logError(ErrorMessages.INVALID_ITEM_TYPE.getErrorMessage());
				return null;
			}

			// generate an item level within relative range
			int chosenItemLevel = ToolsRandom.getRandomIndex((int) itemLevelLowestPossible, (int) itemLevelHighestPossible, plugin);
			if (chosenItemLevel > plugin.getConfig().getInt(ConfigPathNames.MAX_ITEM_LEVEL.getName())) {
				chosenItemLevel = plugin.getConfig().getInt(ConfigPathNames.MAX_ITEM_LEVEL.getName());
			}

			// set the display name of the item
			String list = ConfigPathNames.LIST_NAME.getName();
			String type = (String) itemType;
			String name = ConfigPathNames.ID.getName();
			String itemName = plugin.itemConfig.getString(String.format("%s.%s.%s", list, type, name));
			itemMeta.setDisplayName(itemName);

			// add level, rarity and class to lore
			List<String> lore = new ArrayList<>();
			lore.add(addRarityName(rarity));
			lore.add("");// new line
			lore.add(ChatColor.WHITE + String.format("%s%s", NamesAndPrefixes.CLASS_NAME.getName(), getClassName(restrictedClass)));
			getClassName(restrictedClass);
			lore.add(NamesAndPrefixes.ITEM_LEVEL_PREFIX.getName() + chosenItemLevel);
			itemMeta.setLore(lore);
			itemStack.setItemMeta(itemMeta);
			
			AddEnchantments.enchant(itemStack, rarity, plugin);
			return itemStack;
			
		}
}

package com.gigabytedx.rpg2.inventory;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.ConfigPathNames;
import com.gigabytedx.rpg2.constants.NamesAndPrefixes;

public class InventoryManagement {

	// gets the sum of all item levels of the items in the players hotbar and
	// armor slots
	public static int getGearLevel(PlayerInventory inventory, Main plugin) {
		int totalGearScore = 0;

		totalGearScore += getArmorGearScore(inventory.getArmorContents());
		totalGearScore += getHotbarGearScore(inventory.getContents());

		return totalGearScore / plugin.getConfig().getInt(ConfigPathNames.ITEM_COUNT.getName()) * 10;
	}

	// returns the sum of all items levels in the hotbar
	private static int getHotbarGearScore(ItemStack[] contents) {
		int gearScore = 0;
		for (int i = 0; i <= 8; i++) {
			if (contents[i] != null) {
				gearScore += getGearScoreFromLore(contents[i].getItemMeta().getLore());
			}

		}
		return gearScore;
	}

	// returns the sum of all items levels in the armor slots
	private static int getArmorGearScore(ItemStack[] armorContents) {
		int gearScore = 0;
		for (ItemStack itemStack : armorContents) {
			if (itemStack.getItemMeta() != null) {
				gearScore += getGearScoreFromLore(itemStack.getItemMeta().getLore());
			}

		}
		return gearScore;

	}

	// pulls out the item level from the items lore in its itemMeta
	private static int getGearScoreFromLore(List<String> lore) {
		if (lore != null) {
			for (String lineInLore : lore) {
				if (lineInLore.contains(NamesAndPrefixes.ITEM_LEVEL_PREFIX.getName())) {
					String prefix = NamesAndPrefixes.ITEM_LEVEL_PREFIX.getName();
					int levelIndex = lineInLore.indexOf(prefix) + prefix.length();
					return Integer.parseInt((String) lineInLore.substring(levelIndex));
				}
			}
		}
		return 0;
	}
	
	public static String getPlayerClass(Player player){
		String className = "";
		for (int i = 0; i <= 2; i++) {
			ItemStack item = player.getInventory().getItem(i);
			if (item != null) {
				String itemClass = getGearClassFromLore(item.getItemMeta().getLore());
				if (className.equals("")) {
					className = itemClass;
				}
			}
		}
		className = validateItem(className, player, player.getInventory().getHelmet());
		className = validateItem(className, player, player.getInventory().getChestplate());
		className = validateItem(className, player, player.getInventory().getLeggings());
		className = validateItem(className, player, player.getInventory().getBoots());
		return className;
	}
	
	private static String validateItem(String className, Player player, ItemStack item) {
		String itemClass;
		try {
			itemClass = getGearClassFromLore(item.getItemMeta().getLore());
		} catch (NullPointerException e) {
			// NOT METNIONING AN ERROR HANDLE HERE
			return className;
		}
		if (className.equals("")) {
			className = itemClass;
		}
		return className;
	}
	
	private static String getGearClassFromLore(List<String> lore) {
		if (lore != null) {
			for (String lineInLore : lore) {
				if (lineInLore.contains(NamesAndPrefixes.CLASS_NAME.getName())) {
					String prefix = NamesAndPrefixes.CLASS_NAME.getName();
					int levelIndex = lineInLore.indexOf(prefix) + prefix.length();
					return (String) lineInLore.substring(levelIndex);
				}
			}
		}
		return null;
	}
	
}

package com.gigabytedx.rpg2.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.ConfigPathNames;
import com.gigabytedx.rpg2.tools.ToolsRandom;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;

public class AddEnchantments {

	public static ItemStack enchant(ItemStack itemStack, String rarity, Main plugin) {
		//todo: s
		switch (rarity) {
		case "EXOTIC":
			return addEnchantments(itemStack, 2, true, 5, plugin);
		case "LEGENDARY":
			return addEnchantments(itemStack, 2, false, 4, plugin);
		case "RARE":
			return addEnchantments(itemStack, 1, false, 3, plugin);
		case "UNCOMMON":
			return addEnchantments(itemStack, 1, false, 2, plugin);
		default:
			return itemStack;
		}

	}

	private static ItemStack addEnchantments(ItemStack itemStack, int amount, boolean addCustomEnchantment,
			int maximumLevel, Main plugin) {

		List<CustomEnchantment> enchants = findEnchantment(itemStack, plugin);

		for (int i = 0; i < amount; i++) {
			CustomEnchantment selectedEnchant = enchants.remove(ToolsRandom.getRandomIndex(0, enchants.size() - 1, plugin));
			boolean canAddEnchant = true;
			for (CustomEnchantment currentEnchant : EnchantmentAPI.getAllEnchantments(itemStack).keySet()) {
				System.out.println(currentEnchant.getDescription());
				if (currentEnchant.conflictsWith(selectedEnchant)) {
					System.out.println(currentEnchant + " conflicts with " + selectedEnchant + ": "
							+ currentEnchant.conflictsWith(selectedEnchant));
					i--;
					canAddEnchant = false;
					break;
				}
			}
			if (canAddEnchant) {
				selectedEnchant.addToItem(itemStack, getValidEnchantLevel(maximumLevel, selectedEnchant, plugin));
			}
		}
		return itemStack;
	}

	private static int getValidEnchantLevel(int maximumLevel, CustomEnchantment selectedEnchant, Main plugin) {
		int selectedLevel = ToolsRandom.getRandomIndex(maximumLevel - 1, maximumLevel, plugin);
		while (selectedLevel > selectedEnchant.getMaxLevel()) {
			selectedLevel--;
		}
		return selectedLevel;
	}

	private static List<CustomEnchantment> findEnchantment(ItemStack itemStack, Main plugin) {
		List<CustomEnchantment> validEnchants = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<String> disabledEnchantments = (List<String>) plugin.getConfig()
				.getList(ConfigPathNames.DISABLED_ENCHANTMENTS.getName());
		for (CustomEnchantment enchantment : EnchantmentAPI.getAllValidEnchants(itemStack)) {
				if (!disabledEnchantments.contains(enchantment.name())) {
					validEnchants.add(enchantment);
				}
		}
		System.out.println(itemStack.getType());
		System.out.println(validEnchants);
		return validEnchants;
	}
}

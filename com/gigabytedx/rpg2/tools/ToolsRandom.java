package com.gigabytedx.rpg2.tools;

import java.util.Random;
import java.util.Set;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.ConfigPathNames;
import com.gigabytedx.rpg2.constants.ErrorMessages;

public class ToolsRandom {
	// returns random number between range
	public static int getRandomIndex(int low, int high, Main plugin) {
		try {
			Random r = new Random();
			return r.nextInt(high - low) + low;
		} catch (IllegalArgumentException e) {
			plugin.logError(ErrorMessages.INVALID_NAME.getErrorMessage());
			return 0;
		}
	}

	public static String getRandomRareness(Main plugin) {
		Set<String> rarenessNames = plugin.getConfig().getConfigurationSection(ConfigPathNames.RARITY_WEIGHTS.getName())
				.getKeys(false);

		double totalWeight = 0.0d;
		for (String rarenessName : rarenessNames) {
			totalWeight += plugin.getConfig().getDouble(ConfigPathNames.RARITY_WEIGHTS.getName() + "." + rarenessName);
		}
		double random = Math.random() * totalWeight;
		for (String rarenessName : rarenessNames) {
			random -= plugin.getConfig().getDouble(ConfigPathNames.RARITY_WEIGHTS.getName() + "." + rarenessName);
			if (random <= 0.0d) {
				return rarenessName;
			}
		}
		return "COMMON";

	}

	public static boolean dropItem(Main plugin) {
		double weight = plugin.getConfig().getDouble(ConfigPathNames.DROP_RATE.getName());
		if (getRandomIndex(0, 100, plugin) < weight) {
			return true;
		}
		return false;
	}
}

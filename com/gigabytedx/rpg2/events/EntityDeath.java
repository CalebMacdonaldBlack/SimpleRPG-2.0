package com.gigabytedx.rpg2.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.Classes;
import com.gigabytedx.rpg2.constants.ConfigPathNames;
import com.gigabytedx.rpg2.constants.ErrorMessages;
import com.gigabytedx.rpg2.damagemodiers.ModifyEvent;
import com.gigabytedx.rpg2.displaynames.MobDisplayLevels;
import com.gigabytedx.rpg2.inventory.InventoryManagement;
import com.gigabytedx.rpg2.items.GenerateItem;
import com.gigabytedx.rpg2.tools.ToolsRandom;

public class EntityDeath implements Listener {

	private Main plugin;

	public EntityDeath(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		try {
			MobDisplayLevels.mobHoloMap.get(event.getEntity()).delete();
		} catch (NullPointerException e) {

		}

		event.setDroppedExp(0);
		event.getDrops().clear();
		try {
			int level = (int) ModifyEvent.getEntityLevel(event.getEntity()) / 10;
			System.out.println("LEVEL: " + level);
			if (ToolsRandom.dropItem(plugin))
				event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(),
						getItemStack(level, event.getEntity().getKiller()));
		} catch (NullPointerException e) {
			plugin.logDebug(ErrorMessages.LEVEL_IN_NAME_MISSING.getErrorMessage());
		}
	}

	private ItemStack getItemStack(int level, Player player) {

		String className = null;

		if (player != null) {
			String classNameValue = InventoryManagement.getPlayerClass(player);
			for (String name : Classes.names()) {
				if (Classes.valueOf(name).getClassName().equals(classNameValue)) {
					className = name;
				}
			}

		} else {
			int randomClassIndex = ToolsRandom.getRandomIndex(0, Classes.values().length, plugin);
			className = Classes.names()[randomClassIndex];
		}
		List<String> itemList = new ArrayList<>();

		Set<String> itemTypes = plugin.itemConfig.getConfigurationSection(ConfigPathNames.LIST_NAME.getName())
				.getKeys(false);

		for (String itemType : itemTypes) {
			List<String> classesAvailable = plugin.itemConfig
					.getStringList(ConfigPathNames.LIST_NAME.getName() + "." + itemType + ".class");
			if (classesAvailable.contains(className)) {
				itemList.add(itemType);
			}
		}
		int randomItemIndex = ToolsRandom.getRandomIndex(0, itemList.size(), plugin);
		return GenerateItem.getNewItemFromLevel((String) itemList.get(randomItemIndex),
				ToolsRandom.getRandomRareness(plugin), className, level, plugin);
	}
}

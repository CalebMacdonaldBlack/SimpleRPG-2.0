package com.gigabytedx.rpg2.commands;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.ConfigPathNames;
import com.gigabytedx.rpg2.constants.ErrorMessages;
import com.gigabytedx.rpg2.items.GenerateItem;
import com.gigabytedx.rpg2.tools.ToolsRandom;

public class GetItem implements CommandExecutor {

	Main plugin;

	public GetItem(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Set<String> itemTypes = plugin.itemConfig.getConfigurationSection(ConfigPathNames.LIST_NAME.getName())
					.getKeys(false);
			int randomItemIndex = ToolsRandom.getRandomIndex(0, itemTypes.size(), plugin);
			try {
				if(args[0].equals("1"))
					player.getInventory().addItem(
							GenerateItem.getNewItem(itemTypes.toArray()[randomItemIndex], player.getInventory(), "COMMON", "TANK", plugin));
				else if(args[0].equals("2"))
					player.getInventory().addItem(
							GenerateItem.getNewItem(itemTypes.toArray()[randomItemIndex], player.getInventory(), "UNCOMMON", "TANK", plugin));
				else if(args[0].equals("3"))
					player.getInventory().addItem(
							GenerateItem.getNewItem(itemTypes.toArray()[randomItemIndex], player.getInventory(), "RARE", "TANK", plugin));
				else if(args[0].equals("4"))
					player.getInventory().addItem(
							GenerateItem.getNewItem(itemTypes.toArray()[randomItemIndex], player.getInventory(), "LEGENDARY", "TANK", plugin));
				else if(args[0].equals("5"))
					player.getInventory().addItem(
							GenerateItem.getNewItem(itemTypes.toArray()[randomItemIndex], player.getInventory(), "EXOTIC", "TANK", plugin));
			} catch (IllegalArgumentException e) {
				plugin.logError(ErrorMessages.ITEM_ADD_FAILURE.getErrorMessage());
				player.sendMessage(ErrorMessages.INTERNAL_ERROR.getErrorMessage());
				e.printStackTrace();
			}
		}
		return false;
	}
}

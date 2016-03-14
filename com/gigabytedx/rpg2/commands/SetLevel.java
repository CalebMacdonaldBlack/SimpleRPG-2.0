package com.gigabytedx.rpg2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.ConfirmationMessages;
import com.gigabytedx.rpg2.constants.ErrorMessages;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class SetLevel implements CommandExecutor {

	private Main plugin;

	public SetLevel(Main plugin) {
		this.plugin = plugin;
	}

	// this is not designed well and will break if regions overlap
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			int level;
			Player player = (Player) sender;
			try {
				level = Integer.parseInt(args[0]);
			} catch (Exception e) {
				sender.sendMessage(ErrorMessages.COMMAND_ARGUMENT_ERROR.getErrorMessage()
						+ "Please use a number /setlevel <level>");
				return false;
			}
			// get the list of regions that contain the given location
			RegionManager regionManager = WGBukkit.getRegionManager(player.getLocation().getWorld());
			ApplicableRegionSet set = regionManager.getApplicableRegions(player.getLocation());
			for (ProtectedRegion region : set.getRegions()) {
				plugin.locationConfig.set(region.getId() + ".level", level);
				plugin.saveCustomConfig(plugin.locationConfigFile, plugin.locationConfig);
				player.sendMessage(ConfirmationMessages.SUCCESSFUL_SETLEVEL.getName());
				return true;
			}
			player.sendMessage(ErrorMessages.NO_REGION_ERROR.getErrorMessage());
		} else {
			sender.sendMessage(ErrorMessages.SENDER_ERROR.getErrorMessage());
		}
		return false;
	}

}

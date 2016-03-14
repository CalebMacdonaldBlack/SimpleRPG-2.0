package com.gigabytedx.rpg2.displaynames;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.NamesAndPrefixes;
import com.gigabytedx.rpg2.inventory.InventoryManagement;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class DisplayLevels {
	
	public static HashMap<Player, DisplayLevels> playerHoloMap = new HashMap<>();
	final Hologram hologram;
	public boolean destroy = false;
	
	public DisplayLevels(String text, final Player player, Main plugin, String className){
		hologram = HologramsAPI.createHologram(plugin, player.getLocation().add(0.0, 2.9, 0.0));
		hologram.clearLines();
		hologram.appendTextLine(text);
		hologram.appendTextLine(NamesAndPrefixes.CLASS_NAME_COLOR.getName() + className);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				try{
					if(destroy){
						hologram.delete();
						cancel();
					}else if(Bukkit.getServer().getOnlinePlayers().contains(player)){
						hologram.teleport(player.getLocation().add(0.0,2.9,0.0));
					}else{
						hologram.delete();
						cancel();
					}
				
				}catch(IllegalArgumentException e){
					playerHoloMap.remove(player);
					hologram.delete();
					cancel();
				}
			}
		}.runTaskTimer(plugin, 1L, 1L);
	}
	
	public void delete(){
		destroy = true;
	}

	public static void AddHologram(Player player, Main plugin, String className) {
		int level = InventoryManagement.getGearLevel(player.getInventory(), plugin) / 10;
		player.setLevel(level);
		try {
			DisplayLevels.playerHoloMap.get(player).delete();
		} catch (NullPointerException e) {
			plugin.logDebug("null on deleting");
		}
		DisplayLevels.playerHoloMap.put((Player) player,
				new DisplayLevels(NamesAndPrefixes.MOB_LEVEL_PREFIX.getName() + level, player, plugin, className));
		
	}
}

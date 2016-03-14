package com.gigabytedx.rpg2.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.displaynames.MobDisplayLevels;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class EntitySpawn implements Listener {

	private Main plugin;

	public EntitySpawn(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent event){
		LivingEntity entity = event.getEntity();
		RegionManager regionManager = WGBukkit.getRegionManager(entity.getLocation().getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(entity.getLocation());
		for (ProtectedRegion region : set.getRegions()) {
			int level = plugin.locationConfig.getInt(region.getId() + ".level");
			
			MobDisplayLevels.AddHologram(entity, plugin, level);
			return;
		}
	}

}

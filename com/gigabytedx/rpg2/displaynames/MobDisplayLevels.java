package com.gigabytedx.rpg2.displaynames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.NamesAndPrefixes;
import com.gigabytedx.rpg2.inventory.InventoryManagement;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class MobDisplayLevels {

	public static HashMap<LivingEntity, MobDisplayLevels> mobHoloMap = new HashMap<>();
	public static HashMap<Player, List<Hologram>> holoView = new HashMap<>();
	int level;
	LivingEntity entity;
	public boolean destroy = false;

	public MobDisplayLevels(String text, final LivingEntity entity, Main plugin, int level) {
		this.entity = entity;
		this.level = level;
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			final Hologram holo;
			holo = HologramsAPI.createHologram(plugin, entity.getLocation().add(0.0, 2.6, 0.0));
			holo.clearLines();
			int difference = level - InventoryManagement.getGearLevel(player.getInventory(), plugin) / 10;
			String prefix = null;
			if (difference <= 0) {
				prefix = ChatColor.GREEN + "";
			} else if (difference == 1) {
				prefix = ChatColor.YELLOW + "";
			} else if (difference == 2) {
				prefix = ChatColor.GOLD + "";
			} else if (difference >= 3) {
				prefix = ChatColor.DARK_RED + "";
			}
			holo.appendTextLine(text + prefix + level);
			VisibilityManager visiblityManager = holo.getVisibilityManager();

			visiblityManager.showTo(player);
			visiblityManager.setVisibleByDefault(false);

			List<Hologram> holos;
			try {
				holos = holoView.get(player);

			} catch (NullPointerException e) {
				holos = new ArrayList<>();
				holos.add(holo);
			}

			holoView.put(player, holos);

			new BukkitRunnable() {
				@Override
				public void run() {
					if (destroy) {
						holo.delete();
						cancel();
					} else if (holo.isDeleted()) {
						cancel();
					}
					try {
						if (entity.getLocation().getWorld().getEntities().contains(entity)) {
							holo.teleport(entity.getLocation().add(0.0, 2.6, 0.0));
						} else {
							mobHoloMap.remove(this);
							holo.delete();
							cancel();
						}
					} catch (IllegalArgumentException e) {
						mobHoloMap.remove(this);
						holo.delete();
						cancel();
					}
				}
			}.runTaskTimer(plugin, 1L, 1L);
		}
	}

	public MobDisplayLevels(String text, final LivingEntity entity, Main plugin, int level, Player player) {
		this.entity = entity;
		this.level = level;
		final Hologram holo;
		holo = HologramsAPI.createHologram(plugin, entity.getLocation().add(0.0, 2.6, 0.0));
		holo.clearLines();
		int difference = level - InventoryManagement.getGearLevel(player.getInventory(), plugin) / 10;
		String prefix = null;
		if (difference <= 0) {
			prefix = ChatColor.GREEN + "";
		} else if (difference == 1) {
			prefix = ChatColor.YELLOW + "";
		} else if (difference == 2) {
			prefix = ChatColor.GOLD + "";
		} else if (difference >= 3) {
			prefix = ChatColor.DARK_RED + "";
		}
		holo.appendTextLine(text + prefix + level);
		VisibilityManager visiblityManager = holo.getVisibilityManager();

		visiblityManager.showTo(player);
		visiblityManager.setVisibleByDefault(false);

		List<Hologram> holos;
		try {
			holos = holoView.get(player);
		} catch (NullPointerException e) {
			holos = new ArrayList<>();
			holos.add(holo);
		}

		holoView.put(player, holos);

		new BukkitRunnable() {
			@Override
			public void run() {
				if (destroy) {
					holo.delete();
					cancel();
				} else if (holo.isDeleted()) {
					cancel();
				}
				try {
					if (entity.getLocation().getWorld().getEntities().contains(entity)) {
						holo.teleport(entity.getLocation().add(0.0, 2.6, 0.0));
					} else {
						mobHoloMap.remove(entity);
						holo.delete();
						cancel();
					}
				} catch (IllegalArgumentException e) {
					mobHoloMap.remove(this);
					holo.delete();
					cancel();
				}
			}
		}.runTaskTimer(plugin, 1L, 1L);
	}

	public static void initAllEntities(Main plugin) {
		for (World world : Bukkit.getWorlds()) {
			for (LivingEntity entity : world.getLivingEntities()) {
				if (entity instanceof Creature) {
					RegionManager regionManager = WGBukkit.getRegionManager(entity.getLocation().getWorld());
					ApplicableRegionSet set = regionManager.getApplicableRegions(entity.getLocation());
					for (ProtectedRegion region : set.getRegions()) {
						int level = plugin.locationConfig.getInt(region.getId() + ".level");
						MobDisplayLevels.AddHologram(entity, plugin, level);
					}
				}
			}
		}
	}

	public void delete() {

		this.destroy = true;
	}

	public static void AddHologram(LivingEntity entity, Main plugin, int level) {
		try {
			MobDisplayLevels.mobHoloMap.get(entity).delete();
		} catch (NullPointerException e) {

		}
		MobDisplayLevels.mobHoloMap.put((LivingEntity) entity,
				new MobDisplayLevels(NamesAndPrefixes.MOB_LEVEL_PREFIX.getName(), entity, plugin, level));

	}

	public int getLevel() {
		return level;
	}
	
	//NOT BEING USED
	public static void initAllEntities(Main plugin, Player player) {
		removeAllHolosForPlayer(player);
		for (World world : Bukkit.getWorlds()) {
			for (LivingEntity entity : world.getLivingEntities()) {
				if (entity instanceof Creature) {
					int level;
					try{
						level = mobHoloMap.get(entity).getLevel();
						MobDisplayLevels.AddHologram(entity, plugin, level, player);
					}catch(NullPointerException e){
						RegionManager regionManager = WGBukkit.getRegionManager(entity.getLocation().getWorld());
						ApplicableRegionSet set = regionManager.getApplicableRegions(entity.getLocation());
						for (ProtectedRegion region : set.getRegions()) {
							level = plugin.locationConfig.getInt(region.getId() + ".level");
							MobDisplayLevels.AddHologram(entity, plugin, level, player);
						}
					}
					
				}
			}
		}
	}

	public static void removeAllHolosForPlayer(Player player) {
		try {
			for (Hologram holo : holoView.get(player)) {
				holo.delete();
			}
		} catch (NullPointerException e) {
		}
	}

	private static void AddHologram(LivingEntity entity, Main plugin, int level, Player player) {
		try {
			MobDisplayLevels.mobHoloMap.get(entity).delete();
		} catch (NullPointerException e) {

		}
		MobDisplayLevels.mobHoloMap.put((LivingEntity) entity,
				new MobDisplayLevels(NamesAndPrefixes.MOB_LEVEL_PREFIX.getName(), entity, plugin, level, player));

	}

}

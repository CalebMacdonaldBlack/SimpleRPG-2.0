package com.gigabytedx.rpg2.damagemodiers;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.ConfigPathNames;
import com.gigabytedx.rpg2.displaynames.MobDisplayLevels;
import com.gigabytedx.rpg2.inventory.InventoryManagement;

public class ModifyEvent {
	
	//these two can be removed as they do the same thing
	public static void entityDamagedByPlayer(EntityDamageByEntityEvent event, Main plugin, Player player) {
		double entityLevel = getEntityLevel(event.getEntity());
		double finalDamage = ModifyEvent.getFinalDamage(entityLevel,
				InventoryManagement.getGearLevel(player.getInventory(), plugin), event.getDamage(), player, plugin, true);
		event.setDamage(finalDamage);
	}

	public static void playerDamagedByEntity(EntityDamageByEntityEvent event, Main plugin, Player player) {
		double entityLevel = getEntityLevel(event.getEntity());
		double finalDamage = ModifyEvent.getFinalDamage(entityLevel,
				InventoryManagement.getGearLevel(player.getInventory(), plugin), event.getDamage(), player, plugin, false);
		event.setDamage(finalDamage);
	}

	public static double getFinalDamage(double entityLevel, int gearLevel, double damageDealt, Player player,
			Main plugin, boolean isPlayerDamager) {
		double levelDifference = entityLevel - gearLevel;
		double percentPerLevel = plugin.getConfig().getDouble(ConfigPathNames.BASE_DAMAGE_SCALING.getName()) / 100;
		double totalScaledPercent;
		double scaledDamage = damageDealt;

		if (levelDifference <= 0) {
			logData(entityLevel, gearLevel, damageDealt, player, 0, scaledDamage);
			return damageDealt;
		} else {
			totalScaledPercent = levelDifference * percentPerLevel;
			if (isPlayerDamager)
				scaledDamage -= scaledDamage * totalScaledPercent;
			else {
				scaledDamage += scaledDamage * totalScaledPercent;
			}
			if (scaledDamage < 0) {
				scaledDamage = 0;
				player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 50);
			}

			logData(entityLevel, gearLevel, damageDealt, player, totalScaledPercent, scaledDamage);

			return scaledDamage;
		}
	}

	public static void logData(double entityLevel, int gearLevel, double damageDealt, Player player,
			double totalScaledPercent, double scaledDamage) {
		player.sendMessage(ChatColor.AQUA + "Original Damage: " + ChatColor.GOLD + damageDealt);
		player.sendMessage(ChatColor.AQUA + "Scaled Damage: " + ChatColor.GOLD + scaledDamage);
		player.sendMessage(ChatColor.AQUA + "Scaled Percent: " + ChatColor.GOLD + totalScaledPercent);
		player.sendMessage(ChatColor.AQUA + "Damager Level: " + ChatColor.GOLD + gearLevel / 10);
		player.sendMessage(ChatColor.AQUA + "Entity Level: " + ChatColor.GOLD + entityLevel / 10);
	}

	public static double getEntityLevel(Entity entity) {
		try{
		int level = MobDisplayLevels.mobHoloMap.get(entity).getLevel();
		return level * 10;
		}catch(NullPointerException e){
			System.out.println("null at getEnitiyLevel");
			return 0;
		}
	}

	public static void damagedByArrow(EntityDamageByEntityEvent event, Arrow damager, Main plugin) {
		if (damager.getShooter() instanceof Player) {
			double entityLevel = getEntityLevel(event.getEntity());
			double finalDamage = ModifyEvent.getFinalDamage(entityLevel,
					InventoryManagement.getGearLevel(((Player) damager.getShooter()).getInventory(), plugin), event.getDamage(),
					((Player) damager.getShooter()), plugin, true);
			event.setDamage(finalDamage);
		}else if (damager.getShooter() instanceof LivingEntity) {
			System.out.println("ENTITY: "+ event.getEntity().getType());
			double entityLevel = getEntityLevel((Entity) damager.getShooter());
			double finalDamage = ModifyEvent.getFinalDamage(entityLevel,
					InventoryManagement.getGearLevel(((Player) event.getEntity()).getInventory(), plugin), event.getDamage(),
					((Player) event.getEntity()), plugin, false);
			event.setDamage(finalDamage);
		}

	}
}

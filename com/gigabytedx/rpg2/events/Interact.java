package com.gigabytedx.rpg2.events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.damagemodiers.ModifyEvent;

public class Interact implements Listener {

	private Main plugin;

	public Interact(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			//do not scale for PVP
			//ModifyEvent.playerDamagedByPlayer(event, plugin, (Player) event.getDamager());
		} else if (event.getDamager() instanceof Arrow) {
			ModifyEvent.damagedByArrow(event, (Arrow) event.getDamager(), plugin);
		} else if (event.getDamager() instanceof Player) {
			ModifyEvent.entityDamagedByPlayer(event, plugin, (Player) event.getDamager());
		} else if (event.getEntity() instanceof Player) {
			ModifyEvent.playerDamagedByEntity(event, plugin, (Player) event.getEntity());
		}
	}

	// @EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof LivingEntity) {
			ItemStack item1 = event.getPlayer().getInventory().getItemInMainHand();
			ItemStack item2 = event.getPlayer().getInventory().getItemInOffHand();
			LivingEntity mob = (LivingEntity) event.getRightClicked();
			mob.getEquipment().setItemInMainHand(item1);
			mob.getEquipment().setItemInOffHand(item2);
		}
	}
}

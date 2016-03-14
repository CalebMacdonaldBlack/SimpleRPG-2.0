package com.gigabytedx.rpg2.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.displaynames.DisplayLevels;
import com.gigabytedx.rpg2.displaynames.MobDisplayLevels;
import com.gigabytedx.rpg2.inventory.InventoryManagement;

public class JoinServer implements Listener {

	private Main plugin;

	public JoinServer(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		MobDisplayLevels.initAllEntities(plugin);
		DisplayLevels.AddHologram(event.getPlayer(), plugin, InventoryManagement.getPlayerClass(event.getPlayer()));

		ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(".");
		itemStack.setItemMeta(meta);
		for (int i = 3; i <= 8; i++) {
			event.getPlayer().getInventory().setItem(i, itemStack);
		}
	}
}

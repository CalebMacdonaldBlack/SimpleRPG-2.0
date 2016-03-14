package com.gigabytedx.rpg2.events;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.constants.ErrorMessages;
import com.gigabytedx.rpg2.constants.NamesAndPrefixes;
import com.gigabytedx.rpg2.displaynames.DisplayLevels;
import com.gigabytedx.rpg2.displaynames.MobDisplayLevels;
import com.gigabytedx.rpg2.inventory.InventoryManagement;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class InventoryInteract implements Listener {

	private Main plugin;

	public InventoryInteract(Main plugin) {
		this.plugin = plugin;
	}

	// returns true if player shouldn't edit inventory
	private boolean playerEditedInventory(HumanEntity whoClicked) {
		RegionManager regionManager = WGBukkit.getRegionManager(whoClicked.getLocation().getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(whoClicked.getLocation());
		if (set.size() > 0) {
			whoClicked.sendMessage(ErrorMessages.CHANGE_INVENTORY_IN_REGION.getErrorMessage());
			return true;
		}

		return false;
	}

	@EventHandler
	public void editInv(InventoryClickEvent event) {
		try {
			if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
				return;
			} else if (event.getCursor().getType().equals(Material.STAINED_GLASS_PANE)) {
				event.setCancelled(true);
			} else if (event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
				event.setCancelled(true);
			} else if (playerEditedInventory(event.getWhoClicked())) {
				event.setCancelled(true);
			} else if (event.getHotbarButton() > 2 && event.getHotbarButton() < 9) {
				event.setCancelled(true);
			}
		} catch (NullPointerException e) {
			// UNHANDLED ERROR HERE
		}
	}

	@EventHandler
	public void editInv(InventoryDragEvent event) {
		if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		} else if (event.getCursor() != null && event.getCursor().getType().equals(Material.STAINED_GLASS_PANE)) {
			event.setCancelled(true);
		} else if (event.getOldCursor().getType().equals(Material.STAINED_GLASS_PANE)) {
			event.setCancelled(true);
		} else if (playerEditedInventory(event.getWhoClicked())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void editInv(PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		} else if (event.getItemDrop().getType().equals(Material.STAINED_GLASS_PANE)) {
			event.setCancelled(true);
		} else if (playerEditedInventory(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHoldItemInHand(PlayerItemHeldEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		} else if (event.getNewSlot() > 2 && event.getNewSlot() < 9) {
			ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
			ItemMeta meta = itemStack.getItemMeta();
			meta.setDisplayName(".");
			itemStack.setItemMeta(meta);

			event.getPlayer().getInventory().setItem(event.getNewSlot(), itemStack);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}

		checkInventory(event);
		final Player player = (Player) event.getPlayer();
		int level = InventoryManagement.getGearLevel(player.getInventory(), plugin) / 10;
		player.setLevel(level);
		
		MobDisplayLevels.initAllEntities(plugin);
	}

	@EventHandler
	public void onItemPickUp(PlayerPickupItemEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		checkInventory(event);
		Player player = (Player) event.getPlayer();
		player.setLevel(InventoryManagement.getGearLevel(player.getInventory(), plugin) / 10);
	}

	private void checkInventory(InventoryCloseEvent event) {
		String className = "";
		Player player = (Player) event.getPlayer();

		for (int i = 0; i <= 2; i++) {
			ItemStack item = player.getInventory().getItem(i);
			if (item != null) {
				String itemClass = getGearClassFromLore(item.getItemMeta().getLore());
				if (className.equals("")) {
					className = itemClass;
				}
				if (itemClass != null) {
					if (!className.equals(itemClass)) {
						player.getInventory().setItem(i, null);
						putItemInInventory(item, (Player) player);

					}
				} else {
					player.getInventory().setItem(i, null);
					putItemInInventory(item, (Player) player);
				}
			}
		}
		className = validateItem(className, player, player.getInventory().getHelmet());
		className = validateItem(className, player, player.getInventory().getChestplate());
		className = validateItem(className, player, player.getInventory().getLeggings());
		className = validateItem(className, player, player.getInventory().getBoots());
		
		try {
			DisplayLevels.playerHoloMap.get(event.getPlayer()).delete();
		} catch (NullPointerException e) {

		}
		DisplayLevels.AddHologram(player, plugin, className);
		
	}

	private void checkInventory(PlayerPickupItemEvent event) {
		String className = "";
		Player player = event.getPlayer();
		for (int i = 0; i <= 2; i++) {
			ItemStack item = player.getInventory().getItem(i);
			if (item != null) {
				String itemClass = getGearClassFromLore(item.getItemMeta().getLore());
				if (className.equals("")) {
					className = itemClass;
				}
				if (!className.equals(itemClass)) {
					player.getInventory().setItem(i, null);
					putItemInInventory(item, (Player) player);

				}
			}
		}
		className = validateItem(className, player, player.getInventory().getHelmet());
		className = validateItem(className, player, player.getInventory().getChestplate());
		className = validateItem(className, player, player.getInventory().getLeggings());
		className = validateItem(className, player, player.getInventory().getBoots());

		String itemClass = getGearClassFromLore(event.getItem().getItemStack().getItemMeta().getLore());
		if (!className.equals(itemClass)) {
			putItemInInventory(event.getItem().getItemStack(), (Player) player);
			event.getItem().remove();
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 50);
			event.setCancelled(true);
		}

	}

	private String validateItem(String className, Player player, ItemStack item) {
		String itemClass;
		try {
			itemClass = getGearClassFromLore(item.getItemMeta().getLore());
		} catch (NullPointerException e) {
			// NOT METNIONING AN ERROR HANDLE HERE
			return className;
		}
		if (className.equals("")) {
			className = itemClass;
		}
		if (!className.equals(itemClass)) {

			if (item.getType().toString().contains("HELMET")) {
				player.getInventory().setHelmet(null);
			} else if (item.getType().toString().contains("CHESTPLATE")) {
				player.getInventory().setChestplate(null);
			} else if (item.getType().toString().contains("LEGGINGS")) {
				player.getInventory().setLeggings(null);
			} else if (item.getType().toString().contains("BOOTS")) {
				player.getInventory().setBoots(null);
			}

			putItemInInventory(item, (Player) player);

		}
		return className;
	}

	private void putItemInInventory(ItemStack item, Player player) {
		for (int i = 9; i < 36; i++) {
			if (player.getInventory().getItem(i) == null) {
				player.getInventory().setItem(i, item);
				return;
			}
		}
		player.getWorld().dropItem(player.getLocation(), item);

	}

	@EventHandler
	public void xpChangeEvent(PlayerExpChangeEvent event) {
		event.setAmount(0);
	}

	private static String getGearClassFromLore(List<String> lore) {
		if (lore != null) {
			for (String lineInLore : lore) {
				if (lineInLore.contains(NamesAndPrefixes.CLASS_NAME.getName())) {
					String prefix = NamesAndPrefixes.CLASS_NAME.getName();
					int levelIndex = lineInLore.indexOf(prefix) + prefix.length();
					return (String) lineInLore.substring(levelIndex);
				}
			}
		}
		return null;
	}
}

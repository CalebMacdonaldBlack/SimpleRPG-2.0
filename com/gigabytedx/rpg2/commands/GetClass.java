package com.gigabytedx.rpg2.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.items.GenerateItem;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;

public class GetClass implements CommandExecutor {
	
	Main plugin;
	public GetClass(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		int level = Integer.parseInt(args[1]);
		if(sender instanceof Player){
			if(args[0].equals("knight")){
				addKnightGear((Player) sender, level);
			}else if(args[0].equals("cleric")){
				addClericGear((Player) sender, level);
			}else if(args[0].equals("archer")){
				addArcherGear((Player) sender, level);
			}else if(args[0].equals("bard")){
				addBardGear((Player) sender, level);
			}
		}
		return false;
	}
	private void addBardGear(Player sender, int level) {
		sender.getInventory().addItem(GenerateItem.getNewItem("IRON_AXE", level, "LEGENDARY", "BARD", plugin));
		
		List<CustomEnchantment> enchants = new ArrayList<>();
		enchants.add(EnchantmentAPI.getEnchantment("Motivation"));
		sender.getInventory().addItem(GenerateItem.getNewStaticItem("BOOK", "Motivate Friendlies", level, "LEGENDARY", "BARD", plugin, enchants));
		
		enchants = new ArrayList<>();
		enchants.add(EnchantmentAPI.getEnchantment("Defence Spell"));
		sender.getInventory().addItem(GenerateItem.getNewStaticItem("BOOK", "Boost Defence", level, "LEGENDARY", "BARD", plugin, enchants));
		
		sender.getInventory().addItem(GenerateItem.getNewItem("CHAINMAIL_HELMET", level, "LEGENDARY", "BARD", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("CHAINMAIL_CHESTPLATE", level, "LEGENDARY", "BARD", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("CHAINMAIL_LEGGINGS", level, "LEGENDARY", "BARD", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("CHAINMAIL_BOOTS", level, "LEGENDARY", "BARD", plugin));
	}
	private void addArcherGear(Player sender, int level) {
		sender.getInventory().addItem(GenerateItem.getNewItem("BOW", level, "LEGENDARY", "DPS", plugin));
		
		
		List<CustomEnchantment> enchants = new ArrayList<>();
		enchants.add(EnchantmentAPI.getEnchantment("Shockwave"));
		sender.getInventory().addItem(GenerateItem.getNewStaticItem("BOOK", "Shockwave", level, "LEGENDARY", "DPS", plugin, enchants));
		
		enchants = new ArrayList<>();
		enchants.add(EnchantmentAPI.getEnchantment("Rage"));
		sender.getInventory().addItem(GenerateItem.getNewStaticItem("BOOK", "Boost Attack", level, "LEGENDARY", "DPS", plugin, enchants));
		
		sender.getInventory().addItem(GenerateItem.getNewItem("LEATHER_HELMET", level, "LEGENDARY", "DPS", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("LEATHER_CHESTPLATE", level, "LEGENDARY", "DPS", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("LEATHER_LEGGINGS", level, "LEGENDARY", "DPS", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("LEATHER_BOOTS", level, "LEGENDARY", "DPS", plugin));
		sender.getInventory().addItem(new ItemStack(Material.ARROW, 64));
	}
	private void addClericGear(Player sender, int level) {
		sender.getInventory().addItem(GenerateItem.getNewItem("IRON_SWORD", level, "LEGENDARY", "HEALER", plugin));
		
		List<CustomEnchantment> enchants = new ArrayList<>();
		enchants.add(EnchantmentAPI.getEnchantment("Healing Aura"));
		sender.getInventory().addItem(GenerateItem.getNewStaticItem("BOOK", "Heal friendlies", level, "LEGENDARY", "HEALER", plugin, enchants));
		
		enchants = new ArrayList<>();
		enchants.add(EnchantmentAPI.getEnchantment("Heal Target"));
		sender.getInventory().addItem(GenerateItem.getNewStaticItem("BOOK", "Heal Target", level, "LEGENDARY", "HEALER", plugin, enchants));
		
		sender.getInventory().addItem(GenerateItem.getNewItem("IRON_HELMET", level, "LEGENDARY", "HEALER", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("IRON_CHESTPLATE", level, "LEGENDARY", "HEALER", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("IRON_LEGGINGS", level, "LEGENDARY", "HEALER", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("IRON_BOOTS", level, "LEGENDARY", "HEALER", plugin));
	}
	private void addKnightGear(Player sender, int level) {
		sender.getInventory().addItem(GenerateItem.getNewItem("GOLD_SWORD", level, "LEGENDARY", "TANK", plugin));
		
		List<CustomEnchantment> enchants = new ArrayList<>();
		enchants.add(EnchantmentAPI.getEnchantment("Taunt"));
		sender.getInventory().addItem(GenerateItem.getNewStaticItem("BOOK", "Taunt Enemies", level, "LEGENDARY", "TANK", plugin, enchants));
		
		enchants = new ArrayList<>();
		enchants.add(EnchantmentAPI.getEnchantment("Defence Spell"));
		sender.getInventory().addItem(GenerateItem.getNewStaticItem("BOOK", "Boost Defence", level, "LEGENDARY", "TANK", plugin, enchants));
		
		sender.getInventory().addItem(GenerateItem.getNewItem("DIAMOND_HELMET", level, "LEGENDARY", "TANK", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("DIAMOND_CHESTPLATE", level, "LEGENDARY", "TANK", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("DIAMOND_LEGGINGS", level, "LEGENDARY", "TANK", plugin));
		sender.getInventory().addItem(GenerateItem.getNewItem("DIAMOND_BOOTS", level, "LEGENDARY", "TANK", plugin));
		
	}

}

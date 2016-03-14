package com.gigabytedx.rpg2;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gigabytedx.rpg2.commands.GetClass;
import com.gigabytedx.rpg2.commands.GetItem;
import com.gigabytedx.rpg2.commands.SetLevel;
import com.gigabytedx.rpg2.constants.ConfigPathNames;
import com.gigabytedx.rpg2.constants.NamesAndPrefixes;
import com.gigabytedx.rpg2.displaynames.DisplayLevels;
import com.gigabytedx.rpg2.displaynames.MobDisplayLevels;
import com.gigabytedx.rpg2.events.EntityDeath;
import com.gigabytedx.rpg2.events.EntitySpawn;
import com.gigabytedx.rpg2.events.Interact;
import com.gigabytedx.rpg2.events.InventoryInteract;
import com.gigabytedx.rpg2.events.JoinServer;
import com.gigabytedx.rpg2.events.LeaveServer;
import com.gigabytedx.rpg2.inventory.InventoryManagement;

public class Main extends JavaPlugin {
	public File classConfigFile = new File(getDataFolder() + "/Data/classConfigFile.yml");
	public FileConfiguration classConfig = YamlConfiguration.loadConfiguration(classConfigFile);
	public File itemConfigFile = new File(getDataFolder() + "/Data/itemConfigFile.yml");
	public FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(itemConfigFile);
	public File locationConfigFile = new File(getDataFolder() + "/Data/locationConfigFile.yml");
	public FileConfiguration locationConfig = YamlConfiguration.loadConfiguration(locationConfigFile);
	public static String PLUGIN_NAME;

	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		PLUGIN_NAME = pdfFile.getName();
		Logger logger = getLogger();
		registerEvents();
		registerCommands();
		registerConfig();
		logger.info(pdfFile.getName() + " has been enabled (V." + pdfFile.getVersion() + ")");
		
		for(Player player: Bukkit.getOnlinePlayers()){
			try{
				DisplayLevels.AddHologram(player, this, InventoryManagement.getPlayerClass(player));
			}catch(NullPointerException e){
				
			}
		}
		MobDisplayLevels.initAllEntities(this);
		
	}

	private void registerCommands() {
		getCommand("getclass").setExecutor(new GetClass(this));
		getCommand("getitem").setExecutor(new GetItem(this));
		getCommand("setlevel").setExecutor(new SetLevel(this));
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Interact(this), this);
		pm.registerEvents(new EntitySpawn(this), this);
		pm.registerEvents(new EntityDeath(this), this);
		pm.registerEvents(new InventoryInteract(this), this);
		pm.registerEvents(new JoinServer(this), this);
		pm.registerEvents(new LeaveServer(this), this);
	}

	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			try {
				DisplayLevels.playerHoloMap.get(player).delete();
			} catch (NullPointerException e) {

			}
		}
		
		for(MobDisplayLevels mobDisplayLevel: MobDisplayLevels.mobHoloMap.values()){
			try {
				mobDisplayLevel.delete();
			} catch (NullPointerException e) {

			}
		}

		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();

		logger.info(pdfFile.getName() + " has been disabled (V." + pdfFile.getVersion() + ")");
	}

	public void registerConfig() {
		saveDefaultConfig();
		loadFiles(classConfigFile, classConfig);
		loadFiles(itemConfigFile, itemConfig);
		loadFiles(locationConfigFile, locationConfig);
	}

	public void saveCustomConfig(File file, FileConfiguration fileConfig) {
		try {
			fileConfig.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFiles(File file, FileConfiguration fileConfig) {
		if (file.exists()) {
			try {
				fileConfig.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fileConfig.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void logError(String msg) {
		getLogger().severe(msg);
	}

	public void logDebug(String msg) {
		if (getConfig().getBoolean(ConfigPathNames.DEBUG_ENABLED.getName()) || true) {
			getLogger().info(NamesAndPrefixes.DEBUG_PREFIX.getName() + msg);
		}
	}
}

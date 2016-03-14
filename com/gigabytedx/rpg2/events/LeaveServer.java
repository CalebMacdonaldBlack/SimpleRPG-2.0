package com.gigabytedx.rpg2.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gigabytedx.rpg2.Main;
import com.gigabytedx.rpg2.displaynames.DisplayLevels;

public class LeaveServer implements Listener {

	@SuppressWarnings("unused")
	private Main plugin;

	public LeaveServer(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		try {
			DisplayLevels.playerHoloMap.get(event.getPlayer()).delete();
		} catch (NullPointerException e) {

		}
	}
}

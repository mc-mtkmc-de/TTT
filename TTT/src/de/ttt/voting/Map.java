package de.ttt.voting;

import org.bukkit.Location;

import de.ttt.gamestats.LobbyState;
import de.ttt.main.TTT;
import de.ttt.utils.ConfigLocationUtil;

public class Map {
	
	private TTT plugin;
	private String name;
	private String builder;
	private Location[] spawnLocations = new Location[LobbyState.MAX_PLAYERS];
	private Location spectatorLocation;
	
	public Map(TTT plugin, String name) {
		this.plugin = plugin;
		this.name = name.toUpperCase();
		
	}
	
	public void create(String builder) {
		this.builder = builder;
		plugin.getConfig().set("Arenas." + name + ".Builder", builder);
		plugin.saveConfig();
	}
	
	public boolean exists() {
		return (plugin.getConfig().getString("Arenas." + name + ".Builder") != null);
	}
	
	public void setSpawnLocation( int spawnNumber, Location location) {
		spawnLocations[spawnNumber - 1] = location;
		new ConfigLocationUtil(plugin, location, "Arenas." + name + "." + spawnNumber).saveLocation();
	}
	
	public void setSpectatorLocation(Location location) {
		spectatorLocation = location;
		new ConfigLocationUtil(plugin, location, "Arenas." + name + "." + ".Spectator").saveLocation();
	}
	
	public String getName() {
		return name;
	}
	
	public String getBuilder() {
		return builder;
	}
	
	public Location[] getSpawnLocations() {
		return spawnLocations;
	}
	
	public Location getSpectatorLocation() {
		return spectatorLocation;
	}

}

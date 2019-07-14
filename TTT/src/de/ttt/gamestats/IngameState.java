package de.ttt.gamestats;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.entity.Player;

import de.ttt.countdowns.RoleCountdown;
import de.ttt.main.TTT;
import de.ttt.voting.Map;

public class IngameState extends GameState  {
	
	private TTT plugin;
	private Map map;
	private ArrayList<Player> players;
	private RoleCountdown roleCountdown;
	
	public IngameState(TTT plugin) {
		this.plugin = plugin;
		roleCountdown = new RoleCountdown(plugin);
	}

	@Override
	public void start() {
		Collections.shuffle(plugin.getPlayers());
		players = plugin.getPlayers();
		
		map = plugin.getVoting().getWinnerMap();
		map.load();
		for(int i = 0; i < players.size(); i++)
			players.get(i).teleport(map.getSpawnLocations()[i]);
		
		for(Player current : players) {
			current.getInventory().clear();
			
		}
		
		roleCountdown.start();
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}

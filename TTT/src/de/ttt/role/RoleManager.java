package de.ttt.role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.entity.Player;

import de.ttt.main.TTT;

public class RoleManager {
	
	private TTT plugin;
	private HashMap<String, Role> playerRoles;
	private ArrayList<Player> players;
	
	private int traitors, detectives, innocents;
	
	public RoleManager(TTT plugin) {
		this.plugin = plugin;
		playerRoles = new HashMap<>();
		players = plugin.getPlayers();
		
	}
	
	public void calculateRoles() {
		int playerSize = players.size();
		
		traitors = (int) Math.round(Math.log(playerSize) * 1.2);
		detectives = (int) Math.round(Math.log(playerSize) * 0.75);
		innocents = playerSize - traitors -detectives;
		
		System.out.println("Traitor:" + traitors);
		System.out.println("Detectives:" + detectives);
		System.out.println("Innocents:" + innocents);
		
		Collections.shuffle(plugin.getPlayers());
		
		int counter = 0;
		for(int i = counter; i < traitors; i++)
			playerRoles.put(players.get(i).getName(), Role.TRAITOR);
		counter += traitors;
		
		for( int i = counter; i < detectives; i++)
			playerRoles.put(players.get(i).getName(), Role.DETECTIVE);
		counter += detectives;
		
		for( int i = counter; i < innocents; i++)
			playerRoles.put(players.get(i).getName(), Role.INNOCENT);
		counter += innocents;
	}
	
	public Role getPlayerRole(Player player) {
		return playerRoles.get(player.getName());
	}

}

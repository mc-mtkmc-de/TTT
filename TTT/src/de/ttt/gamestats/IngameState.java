package de.ttt.gamestats;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import de.ttt.countdowns.RoleCountdown;
import de.ttt.listeners.MapReset;
import de.ttt.main.TTT;
import de.ttt.role.Role;
import de.ttt.voting.Map;

public class IngameState extends GameState  {
	
	private TTT plugin;
	private Map map;
	private ArrayList<Player> players, spectators;
	private RoleCountdown roleCountdown;
	private boolean grace;
	
	private Role winningRole;
	
	public IngameState(TTT plugin) {
		this.plugin = plugin;
		roleCountdown = new RoleCountdown(plugin);
		spectators = new ArrayList<>();
	}

	@Override
	public void start() {
		grace = true;
		
		Collections.shuffle(plugin.getPlayers());
		players = plugin.getPlayers();
		
		map = plugin.getVoting().getWinnerMap();
		map.load();
		for(int i = 0; i < players.size(); i++)
			players.get(i).teleport(map.getSpawnLocations()[i]);
		
		for(Player current : players) {
			current.setHealth(20);
			current.setFoodLevel(20);
			current.getInventory().clear();
			current.setGameMode(GameMode.SURVIVAL);
			plugin.getGameProtectionListener().getBuildModePlayers().remove(current.getName());
			
		}
		
		roleCountdown.start();
		
	}
	
	public void checkGameEnding() {
		if(plugin.getRoleManager().getTraitorPlayers().size() <= 0) {
			winningRole = Role.INNOCENT;
			plugin.getGameStateManager().setGameState(ENDING_STATE);
		} else if(plugin.getRoleManager().getTraitorPlayers().size() == plugin.getPlayers().size()) {
			winningRole = Role.TRAITOR;
			plugin.getGameStateManager().setGameState(ENDING_STATE);
		}
	}
	
	public void addSpectator(Player player) {
		spectators.add(player);
		player.setGameMode(GameMode.CREATIVE);
		player.teleport(map.getSpectatorLocation()); 
		
		for(Player current : Bukkit.getOnlinePlayers())
			current.hidePlayer(player);
	}

	@Override
	public void stop() {
		Bukkit.broadcastMessage(TTT.PREFIX + "§7Das Spiel ist aus!");
		Bukkit.broadcastMessage(TTT.PREFIX + "§6Sieger: " + winningRole.getChatColor() + winningRole.getName());
		MapReset.restore();
		
	}
	
	public void setGrace(boolean grace) {
		this.grace = grace;
	}
	
	public boolean isInGrace() {
		return grace;
	}
	
	public ArrayList<Player> getSpectators() {
		return spectators;
	}

}

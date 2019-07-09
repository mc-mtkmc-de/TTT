package de.ttt.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.ttt.commands.SetupCommand;
import de.ttt.gamestats.GameState;
import de.ttt.gamestats.GameStateManager;
import de.ttt.listeners.PlayerLobbyConnectionListener;

public class TTT extends JavaPlugin {
	
	public static final String PREFIX = "§7[§cTTT§7] §r",
				 			   NO_PERMISSION = PREFIX + "§cDazu hast du keine Rechte!";
	
	private GameStateManager gameStateManager;
	private ArrayList<Player> players;
	
	@Override
	public void onEnable() {
		
		gameStateManager = new GameStateManager(this);
		players = new ArrayList<>();
		
		gameStateManager.setGameState(GameState.LOBBY_STATE);
		
		init(Bukkit.getPluginManager());
		
		System.out.println("[TTT] Das Plugin wurde gestartet.");
	}
	
	private void init(PluginManager pluginManager) {
		
		getCommand("setup").setExecutor(new SetupCommand(this));
		
		pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
	}
	
	@Override
	public void onDisable() {
		System.out.println("[TTT] Das Plugin wurde beendet.");
	}
	
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

}

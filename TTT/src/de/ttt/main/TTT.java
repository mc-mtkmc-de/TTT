package de.ttt.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.ttt.commands.BuildCommand;
import de.ttt.commands.SetupCommand;
import de.ttt.commands.StartCommand;
import de.ttt.gamestats.GameState;
import de.ttt.gamestats.GameStateManager;
import de.ttt.listeners.GameProgressListener;
import de.ttt.listeners.GameProtectionListener;
import de.ttt.listeners.PlayerLobbyConnectionListener;
import de.ttt.listeners.VotingListener;
import de.ttt.role.RoleManager;
import de.ttt.voting.Map;
import de.ttt.voting.Voting;

public class TTT extends JavaPlugin {
	
	public static final String PREFIX = "§7[§cTTT§7] §r",
				 			   NO_PERMISSION = PREFIX + "§cDazu hast du keine Rechte!";
	
	private GameStateManager gameStateManager;
	private ArrayList<Player> players;
	private ArrayList<Map> maps;
	private Voting voting;
	private RoleManager roleManager;
	private GameProtectionListener gameProtectionListener;
	
	@Override
	public void onEnable() {
		
		gameStateManager = new GameStateManager(this);
		players = new ArrayList<>();
		
		gameStateManager.setGameState(GameState.LOBBY_STATE);
		
		init(Bukkit.getPluginManager());
		
		System.out.println("[TTT] Das Plugin wurde gestartet.");
	}
	
	private void init(PluginManager pluginManager) {
		initVoting();
		roleManager = new RoleManager(this);
		gameProtectionListener = new GameProtectionListener(this);
		
		getCommand("setup").setExecutor(new SetupCommand(this));
		getCommand("start").setExecutor(new StartCommand(this));
		getCommand("build").setExecutor(new BuildCommand(this));
		
		pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
		pluginManager.registerEvents(new VotingListener(this), this);
		pluginManager.registerEvents(new GameProgressListener(this), this);
		pluginManager.registerEvents(gameProtectionListener, this);
	}
	
	private void initVoting() {
		maps = new ArrayList<>();
		for(String current : getConfig().getConfigurationSection("Arenas").getKeys(false)) {
			Map map = new Map(this, current);
			if(map.playable())
				maps.add(map);
			else
				Bukkit.getConsoleSender().sendMessage("§cDie Map §4" + map.getName() + " §cist noch nicht fertig eingerichtet!");
		}
		if(maps.size() >= Voting.MAP_AMOUNT)
			voting = new Voting(this, maps);
		else {
			Bukkit.getConsoleSender().sendMessage("§cFür das Voting müssen mindestens §b" + Voting.MAP_AMOUNT + " §cMaps eingerichtet sein!");
			voting = null;
		}
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
	
	public Voting getVoting() {
		return voting;
	}
	
	public ArrayList<Map> getMaps() {
		return maps;
	}
	
	public RoleManager getRoleManager() {
		return roleManager;
	}
	
	public GameProtectionListener getGameProtectionListener() {
		return gameProtectionListener;
	}

}

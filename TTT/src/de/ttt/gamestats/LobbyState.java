package de.ttt.gamestats;

import org.bukkit.Bukkit;

import de.ttt.countdowns.LobbyCountdown;
import de.ttt.main.TTT;

public class LobbyState extends GameState {
	
	public static final int MIN_PLAYERS = 2,
							MAX_PLAYERS = 12;
	
	private LobbyCountdown countdown;
	
	public LobbyState(GameStateManager gameStateManager) {
		countdown = new LobbyCountdown(gameStateManager);
	}

	@Override
	public void start() {
		countdown.startIdle();
	}

	@Override
	public void stop() {
		Bukkit.broadcastMessage(TTT.PREFIX + "�cAlle Spieler werden Teleportiert!");
	}
	
	public LobbyCountdown getCountdown() {
		return countdown;
	}

}

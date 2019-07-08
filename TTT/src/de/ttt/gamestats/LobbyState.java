package de.ttt.gamestats;

import org.bukkit.Bukkit;

import de.ttt.countdowns.LobbyCountdown;

public class LobbyState extends GameState {
	
	public static final int MIN_PLAYERS = 1,
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
		Bukkit.broadcastMessage("Wir wären jetzt im IngameState!");
	}
	
	public LobbyCountdown getCountdown() {
		return countdown;
	}

}

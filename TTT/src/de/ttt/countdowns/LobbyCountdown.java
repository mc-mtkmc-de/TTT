package de.ttt.countdowns;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;

import de.ttt.gamestats.GameState;
import de.ttt.gamestats.GameStateManager;
import de.ttt.gamestats.LobbyState;
import de.ttt.main.TTT;
import de.ttt.voting.Map;
import de.ttt.voting.Voting;

public class LobbyCountdown extends Countdown {
	
	private static final int COUNTDOWN_TIME = 60, IDLE_TIME = 15;
	
	private GameStateManager gameStateManager;
	
	private int seconds;
	private boolean isRunning;
	private int idleID;
	private boolean isIdling;
	
	public LobbyCountdown(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		seconds = COUNTDOWN_TIME;
		
	}

	@Override
	public void start() {
		isRunning = true;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				switch(seconds) {
				case 60: case 45: case 30: case 20: case 10: case 5: case 3: case 2:
					Bukkit.broadcastMessage(TTT.PREFIX + "§7Das Spiel startet in §a" + seconds + " Sekunden§7!");
					
					if(seconds == 3) {
						Voting voting = gameStateManager.getPlugin().getVoting();
						Map winningMap;
						if(voting != null)
							winningMap = voting.getWinnerMap();
							
						else {
							ArrayList<Map> maps = gameStateManager.getPlugin().getMaps();
							Collections.shuffle(maps);
							winningMap = maps.get(0);
						}
						
						Bukkit.broadcastMessage(TTT.PREFIX + "§6Sieger des Votings: §b" + winningMap.getName());
						
					}
					
					break;
				case 1:
					Bukkit.broadcastMessage(TTT.PREFIX + "§7Das Spiel startet in §aeiner Sekunde§7!");
					break;
				case 0:
					gameStateManager.setGameState(GameState.INGAME_STATE);
					break;
					
					default:
						break;
				}
				seconds--;
			}
		}, 0, 20);
		
	}

	@Override
	public void stop() {
		if(isRunning) {
			Bukkit.getScheduler().cancelTask(taskID);
			isRunning = false;
			seconds = COUNTDOWN_TIME;
		}
		
	}
	
	public void startIdle() {
		isIdling = true;
		idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				Bukkit.broadcastMessage(TTT.PREFIX + "§7Bis zum Spielstart fehlen noch §6" + 
										(LobbyState.MIN_PLAYERS - gameStateManager.getPlugin().getPlayers().size()) + 
										" Spieler§7!");
			}
		}, 0, 20 * IDLE_TIME);
	}
	
	public void stopIdle() {
		if(isIdling) {
			Bukkit.getScheduler().cancelTask(idleID);
			isIdling = false;
		}
		
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

}

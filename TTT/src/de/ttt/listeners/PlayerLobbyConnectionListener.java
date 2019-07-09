package de.ttt.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ttt.countdowns.LobbyCountdown;
import de.ttt.gamestats.LobbyState;
import de.ttt.main.TTT;
import de.ttt.utils.ConfigLocationUtil;

public class PlayerLobbyConnectionListener  implements Listener {
	
	private TTT plugin;
	
	public PlayerLobbyConnectionListener(TTT plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handlePlayerJoin(PlayerJoinEvent event) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
		Player player = event.getPlayer();
		plugin.getPlayers().add(player);
		event.setJoinMessage(TTT.PREFIX + "§a" + player.getDisplayName() + " §7ist dem Spiel beigetreten! ["  +
							plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + "]");
		
		ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "Lobby");
		if(locationUtil.loadLocation() != null) {
			player.teleport(locationUtil.loadLocation());
		} else
			Bukkit.getConsoleSender().sendMessage("§cDie Lobby-Location wurde noch nicht gesetzt!");
		
		LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
		LobbyCountdown countdown = lobbyState.getCountdown();
		if(plugin.getPlayers().size() >= LobbyState.MIN_PLAYERS) {
			if(!countdown.isRunning()) {
				countdown.stopIdle();
				countdown.start();
			}
		}
			
	}
	
	@EventHandler
	public void handePlayerQuit(PlayerQuitEvent event) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
		Player player = event.getPlayer();
		plugin.getPlayers().remove(player);
		event.setQuitMessage(TTT.PREFIX + "§c" + player.getDisplayName() + " §7hat das Spiel verlassen! ["  +
							plugin.getPlayers().size() + "/" + LobbyState.MAX_PLAYERS + "]");
		
		LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
		LobbyCountdown countdown = lobbyState.getCountdown();
		if(plugin.getPlayers().size() < LobbyState.MIN_PLAYERS) {
			if(countdown.isRunning()) {
				countdown.stop();
				countdown.startIdle();
			}
		}
	}

}

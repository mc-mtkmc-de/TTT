package de.ttt.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.ttt.gamestats.IngameState;
import de.ttt.main.TTT;
import de.ttt.role.Role;

public class ChatListener implements Listener {
	
	private TTT plugin;
	
	public ChatListener(TTT plugin) {
		this.plugin = plugin;
		
	}
	
	@EventHandler
	public void handleDefaultChat(AsyncPlayerChatEvent event) {
		if(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState) return;
		event.setFormat(getChatFormat(ChatColor.GOLD, event.getPlayer()) + event.getMessage());
	}
	
	@EventHandler
	public void handleIngameState(AsyncPlayerChatEvent event) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) return;
		
		IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
		Player player = event.getPlayer();
		
		if(ingameState.isInGrace()) {
			event.setFormat(getChatFormat(ChatColor.GOLD, event.getPlayer()) + event.getMessage());
			return;
		}
		
		if(ingameState.getSpectators().contains(player)) {
			event.setCancelled(true);
			for(Player current : ingameState.getSpectators())
				current.sendMessage(getChatFormat(ChatColor.DARK_GRAY, player) + event.getMessage());
			return;
		}
		
		Role playerRole = plugin.getRoleManager().getPlayerRole(player);
		if((playerRole == Role.DETECTIVE) || (playerRole == Role.INNOCENT)) {
			event.setFormat(getChatFormat(playerRole.getChatColor(), player) + event.getMessage());
			return;
		}
		
		if(playerRole == Role.TRAITOR) {
			event.setCancelled(true);
			for(Player current : Bukkit.getOnlinePlayers()) {
				Role currentRole = plugin.getRoleManager().getPlayerRole(current);
				if(currentRole == Role.TRAITOR)
					current.sendMessage(getChatFormat(Role.TRAITOR.getChatColor(), player) + event.getMessage());
				else
					current.sendMessage(getChatFormat(Role.INNOCENT.getChatColor(), player) +event.getMessage());
			}
		}
		
	}
	
	private String getChatFormat(ChatColor playerColor, Player player) {
		return "§7[" + playerColor + player.getName() + "§7] §6>> §7";
	}

}

package de.ttt.countdowns;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.ttt.gamestats.IngameState;
import de.ttt.main.TTT;
import de.ttt.role.Role;

public class RoleCountdown extends Countdown {
	
	private TTT plugin;
	private int seconds = 30;

	public RoleCountdown(TTT plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void start() {
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				switch(seconds) {
				
				case 30:
					Bukkit.broadcastMessage(TTT.PREFIX + "�7Die �bRollen �7werden in �b" + seconds + "Sekunden �7bekannt gegeben!");
					break;
				case 15: case 10: case 5: case 3: case 2:
					Bukkit.broadcastMessage(TTT.PREFIX + "�7Noch �b" + seconds + "Sekunden �7 bis zur �cRollenvergabe!");
					break;
				case 1:
					Bukkit.broadcastMessage(TTT.PREFIX + "�7Noch �beine Sekunde �7 bis zur �cRollenvergabe!");
					break;
					
				case 0:
					stop();
					IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
					ingameState.setGrace(false);
					
					Bukkit.broadcastMessage(TTT.PREFIX + "�aDie Rollen wurden bekannt gegeben!");
					plugin.getRoleManager().calculateRoles(plugin.getPlayers().size());
					
					ArrayList<String> traitorPlayers = plugin.getRoleManager().getTraitorPlayers();
					for(Player current : plugin.getPlayers()) {
						Role playerRole = plugin.getRoleManager().getPlayerRole(current);
						current.sendMessage("�7Deine Rolle: �l" + playerRole.getChatColor() + playerRole.getName());
						current.setDisplayName(playerRole.getChatColor() + current.getName());
						
						if(playerRole == Role.TRAITOR)
							current.sendMessage("�7Die Traitor sind: �c�l" + String.join(", ", traitorPlayers));
					}
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
		Bukkit.getScheduler().cancelTask(taskID);
		
	}

}

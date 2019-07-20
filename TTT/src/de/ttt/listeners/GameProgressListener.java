package de.ttt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.ttt.gamestats.IngameState;
import de.ttt.main.TTT;
import de.ttt.role.Role;
import de.ttt.role.RoleManager;

public class GameProgressListener implements Listener {
	
	private TTT plugin;
	private RoleManager roleManager;
	
	public GameProgressListener(TTT plugin) {
		this.plugin = plugin;
		this.roleManager = plugin.getRoleManager();
	}
	
	@EventHandler
	public void handlePlayerDamage(EntityDamageByEntityEvent event) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState));
		if(!(event.getDamager() instanceof Player)) return;
		if(!(event.getEntity() instanceof Player)) return;
		Player damager = (Player) event.getDamager(), victim = (Player) event.getEntity();
		Role damagerRole = roleManager.getPlayerRole(damager), victimRole = roleManager.getPlayerRole(victim);
		
		if((damagerRole == Role.INNOCENT || damagerRole == Role.DETECTIVE) && victimRole == Role.DETECTIVE)
			damager.sendMessage(TTT.PREFIX + "§cAchtung!! Du hast einen Detectiv angegriffen!");
		if(damagerRole == Role.TRAITOR && victimRole == Role.TRAITOR)
			event.setDamage(0);
	}
	
	@EventHandler
	public void handlePlayerDeath(PlayerDeathEvent event) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) return;
		IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
		Player victim = event.getEntity();
		if(victim.getKiller() == null) return;
		Player killer = victim.getKiller();
		Role killerRole = roleManager.getPlayerRole(killer), victimRole = roleManager.getPlayerRole(victim);
		
		switch(killerRole) {
		case TRAITOR:
			if(victimRole == Role.TRAITOR) {
				killer.sendMessage(TTT.PREFIX + "§cDu hast einen Traitor-Kollegen umgebracht!");
			} else {
				killer.sendMessage(TTT.PREFIX + "§bDu hat einen " + victimRole.getChatColor() + victimRole.getName() + " §bgetötet!");
			}
			break;
		case INNOCENT: case DETECTIVE:
			if(victimRole == Role.TRAITOR) {
				killer.sendMessage(TTT.PREFIX + "§aDu hast einen §cTraitor §agetötet!");
			} else if(victimRole == Role.INNOCENT) {
				killer.sendMessage(TTT.PREFIX + "§cDu hast einen §aInnocent §cermordet!");
			} else if(victimRole == Role.DETECTIVE) {
				killer.sendMessage(TTT.PREFIX + "§cDu hast einen §2Detectiv §cermordet!");
			}
			break;
			
			default:
				break;
		}
		
		victim.sendMessage(TTT.PREFIX + "§7Du wurdest vom " + killerRole.getChatColor() + killerRole.getName() + "§c" + killer.getName() + "umgebracht");
		if(victimRole == Role.TRAITOR)
			plugin.getRoleManager().getTraitorPlayers().remove(victim.getName());
		plugin.getPlayers().remove(victim);
		
		ingameState.checkGameEnding();
	}

}

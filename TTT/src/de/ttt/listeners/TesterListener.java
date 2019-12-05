package de.ttt.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.ttt.gamestats.IngameState;
import de.ttt.main.TTT;
import de.ttt.role.Tester;

public class TesterListener implements Listener {
	
	private TTT plugin;
	
	public TesterListener(TTT plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void handleTesterClick(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block clicked = event.getClickedBlock();
		if((clicked.getType() != Material.WOOD_BUTTON) && (clicked.getType() != Material.STONE_BUTTON)) return;
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) return;
		IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
		if(ingameState.isInGrace()) return;
		
		Tester tester = ingameState.getMap().getTester();
		if(tester.getButton().getLocation().equals(clicked.getLocation()))
			tester.test(event.getPlayer());
		
	}

}

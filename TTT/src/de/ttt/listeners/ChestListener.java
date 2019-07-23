package de.ttt.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.ttt.gamestats.IngameState;
import de.ttt.main.TTT;

public class ChestListener implements Listener {
	
	private TTT plugin;
	private ItemStack woodenSword, stoneSword, ironSword, bow, arrows;
	
	public ChestListener(TTT plugin) {
		this.plugin = plugin;
		
		woodenSword = new ItemStack(Material.WOOD_SWORD);
		stoneSword = new ItemStack(Material.STONE_SWORD);
		ironSword = new ItemStack(Material.IRON_SWORD);
		bow = new ItemStack(Material.BOW);
		arrows =new ItemStack(Material.ARROW, 32);
	}
	
	@EventHandler
	public void handleChestClick(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(event.getClickedBlock().getType() != Material.CHEST) return;
		event.setCancelled(true);
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) return;
		
		Player player = event.getPlayer();
		if(!player.getInventory().contains(woodenSword))
			openChest(woodenSword, event.getClickedBlock(), player);
		else if(!player.getInventory().contains(bow)) {
			openChest(bow, event.getClickedBlock(), player);
			player.getInventory().addItem(arrows);
		} else if(!player.getInventory().contains(stoneSword))
			openChest(stoneSword, event.getClickedBlock(), player);
	}
	
	@EventHandler
	public void handleIronChestClickt(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(event.getClickedBlock().getType() != Material.ENDER_CHEST) return;
		event.setCancelled(true);
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)) return;
		
		IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
		Player player = event.getPlayer();
		if(!ingameState.isInGrace())
			openChest(ironSword, event.getClickedBlock(), player);
		
		else
			player.sendMessage(TTT.PREFIX + "§cDiese Kiste kannst du nach der Schutzzeit öffnen!");
	}
	
	private void openChest(ItemStack item, Block block, Player player) {
		player.getInventory().addItem(item);
		block.setType(Material.AIR);
	}

}

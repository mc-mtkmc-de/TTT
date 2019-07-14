package de.ttt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.ttt.main.TTT;
import de.ttt.voting.Voting;

public class VotingListener implements Listener {
	
	private TTT plugin;
	private Voting voting;
	
	public VotingListener(TTT plugin) {
		this.plugin = plugin;
		voting = plugin.getVoting();
	}
	
	@EventHandler
	public void handleVotingMenuOpener(PlayerInteractEvent event) {
		if(!(event.getAction() == Action.RIGHT_CLICK_AIR)) return;
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if(item.getItemMeta() == null) return;
		if(item.getItemMeta().getDisplayName().equals(PlayerLobbyConnectionListener.VOTING_ITEM_NAME)) {
			player.openInventory(voting.getVotingInventory());
		}

	}
	
	@EventHandler
	public void handleVotingClick(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) return;
		Player player = (Player) event.getWhoClicked();
		if(!event.getInventory().getTitle().equals(Voting.VOTING_INVENTORY_TITLE)) return;
		event.setCancelled(true);
		for(int i = 0; i < voting.getVotingInventoryOrder().length; i++) {
			if(voting.getVotingInventoryOrder()[i] == event.getSlot()) {
				voting.vote(player, i);
				return;
			}
		}
	}

}

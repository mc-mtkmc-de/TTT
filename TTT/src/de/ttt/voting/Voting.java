package de.ttt.voting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.ttt.main.TTT;
import de.ttt.utils.ItemBuilder;

public class Voting {
	
	public static final int MAP_AMOUNT = 2;
	public static final String VOTING_INVENTORY_TITLE = "§b§lVoting";
	
	private TTT plugin;
	private ArrayList<Map> maps;
	private Map[] votingMaps;
	private int[] votingInventoryOrder = new int[]{3, 5};
	private HashMap<String, Integer> playerVotes;
	private Inventory votingInventory;
	
	public Voting(TTT plugin, ArrayList<Map> maps) {
		this.plugin = plugin;
		this.maps = maps;
		votingMaps = new Map[Voting.MAP_AMOUNT];
		playerVotes = new HashMap<>();
		
		chooseRandomMaps();
		initVotingInventory();
	}
	
	private void chooseRandomMaps() {
		for(int i = 0; i < votingMaps.length; i++) {
			Collections.shuffle(maps);
			votingMaps[i] = maps.remove(0);
		}
	}
	
	public void initVotingInventory() {
		votingInventory = Bukkit.createInventory(null, 9*1, Voting.VOTING_INVENTORY_TITLE);
		for(int i = 0; i < votingMaps.length; i++) {
			Map currentMap = votingMaps[i];
			votingInventory.setItem(votingInventoryOrder[i], new ItemBuilder(Material.PAPER).setDisplayName("§b" + currentMap.getName())
															 .setDisplayName("§b" + currentMap.getName() + "§c - §c§l" + currentMap.getVotes() + " Votes")
															 .setLore(" ", "§7Erbauer: §a" + currentMap.getBuilder()).build());
		}
	}
	
	public Map getWinnerMap() {
		Map winnerMap = votingMaps[0];
		for(int i = 1; i < votingMaps.length; i++) {
			if(votingMaps[i].getVotes() >= winnerMap.getVotes())
				winnerMap = votingMaps[i];
		}
		return winnerMap;
	}
	
	public void vote(Player player, int votingMap) {
		if(!playerVotes.containsKey(player.getName())) {
			votingMaps[votingMap].addVote();
			player.closeInventory();
			player.sendMessage(TTT.PREFIX + "§aDu hast für die Map §b" + votingMaps[votingMap].getName() + " §aabgestimt!");
			playerVotes.put(player.getName(), votingMap);
			initVotingInventory();
		} else
			player.sendMessage(TTT.PREFIX + "§cDu hast bereits gevotet");
	}
	
	public Map[] getVotingMaps() {
		return votingMaps;
	}
	
	public HashMap<String, Integer> getPlayerVotes() {
		return playerVotes;
	}
	
	public Inventory getVotingInventory() {
		return votingInventory;
	}
	
	public int[] getVotingInventoryOrder() {
		return votingInventoryOrder;
	}

}

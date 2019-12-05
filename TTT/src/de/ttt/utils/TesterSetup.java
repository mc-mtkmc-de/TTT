package de.ttt.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import de.ttt.main.TTT;
import de.ttt.voting.Map;

public class TesterSetup implements Listener {
	
	private TTT plugin;
	private Player player;
	private Map map;
	private int phase;
	private boolean finished;
	
	private Block[] borderBlocks, lamps;
	private Block button;
	private Location testerLocation;
	
	public TesterSetup(Player player,Map map, TTT plugin) {
		this.plugin = plugin;
		this.map = map;
		this.player = player;
		phase = 1;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		borderBlocks = new Block[3];
		lamps = new Block[2];
		finished = false;
		
		startSetup();
	}
	
	@EventHandler
	public void handleBlockBreak(BlockBreakEvent event) {
		if(!event.getPlayer().getName().equals(player.getName())) return;
		if(finished) return;
		event.setCancelled(true);
		switch(phase) {
		case 1: case 2: case 3:
			borderBlocks[phase-1] = event.getBlock();
			player.sendMessage("§6Grenzblock " + phase + " §agesetzt");
			phase++;
			startPhase(phase);
			break;
			
		case 4: case 5:
			if(event.getBlock().getType() == Material.GLASS) {
				lamps[phase-4] = event.getBlock();
				player.sendMessage("§6Lampenblock " + (phase-3) + " §agesetzt");
				phase++;
			} else
				player.sendMessage("cDie Lampe muss aus §6Glas §cbestehen!");
			break;
			
		case 6:
			if((event.getBlock().getType() == Material.STONE_BUTTON) || (event.getBlock().getType() == Material.WOOD_BUTTON)) {
				button = event.getBlock();
				player.sendMessage("§6Du hast den §6Testerknopf §agesetzt");
				phase++;
			} else
				player.sendMessage("§cBitte klicke einen Knopf an!");
		}
	}
	
	@EventHandler
	public void handlePlayerSneak(PlayerToggleSneakEvent event) {
		if(!event.getPlayer().getName().equals(player.getName())) return;
		if(finished) return;
		if(phase == 7) {
			testerLocation = player.getLocation();
			player.sendMessage("§aDie §6Tester-Location §awurde gesetzt!");
			finishSetup();
		}
	}
	
	public void finishSetup() {
		player.sendMessage("§aDas Setup wurde abgeschlossen!");
		finished = true;
		
		for(int i = 0; i < borderBlocks.length; i++)
			new ConfigLocationUtil(plugin, borderBlocks[i].getLocation(), "Arenas." + map.getName() + ".Tester.Borderblocks.").saveBlockLocation();
		
		for(int i = 0; i < lamps.length; i++)
			new ConfigLocationUtil(plugin, lamps[i].getLocation(), "Arenas." + map.getName() + ".Tester.Lampss.").saveBlockLocation();
		
		new ConfigLocationUtil(plugin, button.getLocation(), "Arenas." + map.getName() + ".Tester.Button.").saveBlockLocation();
		new ConfigLocationUtil(plugin, testerLocation, "Arenas." + map.getName() + ".Tester.Location.").saveLocation();
	}
	
	public void startPhase(int phase) {
		switch(phase) {
		case 1: case 2: case 3:
			player.sendMessage("§7Bitte klicke einen §6Begrenzungsblock §7an!");
			break;
		case 4: case 5:
			player.sendMessage("§7Bitte klicke eine §6Lampe §7an!");
			break;
		case 6:
			player.sendMessage("§7Bitte klicke den §6Testknopf §7an!");
			break;
		case 7:
			player.sendMessage("§7Bitte §6sneake §7an der Teseter-Location!");
			break;
		}
	}
	
	public void startSetup() {
		player.sendMessage("§aDu hast einen Tester-Setup gestartet.");
		player.sendMessage("§6Starte mit Schtritt 1");
		startPhase(phase);
	}

}

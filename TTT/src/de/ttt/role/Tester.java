package de.ttt.role;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.ttt.main.TTT;
import de.ttt.utils.ConfigLocationUtil;
import de.ttt.voting.Map;

public class Tester {
	
	private static final int TESTING_TIME = 5;
	
	private TTT plugin;
	private Map map;
	
	private Block[] borderBlocks, lamps;
	private Block button;
	private Location testerLocation;
	private boolean inUse;
	private World world;
	
	public Tester(Map map, TTT plugin) {
		this.plugin = plugin;
		this.map = map;
		
		borderBlocks = new Block[3];
		lamps = new Block[2];
	}
	
	public void test(Player player) {
		Role role = plugin.getRoleManager().getPlayerRole(player);
		if(role == Role.DETECTIVE) {
			player.sendMessage(TTT.PREFIX + "§cDu bist Detectiv und kannst den Tester nicht benutzen!");
			return;
		}
		
		if(inUse) {
			player.sendMessage(TTT.PREFIX + "§cDer Tester ist schon in Benutzung!");
			return;
		}
		
		Bukkit.broadcastMessage(TTT.PREFIX + "§7Ein Spieler hat den Tester betreten!");
		player.teleport(button.getLocation());
		inUse = true;
		for(Block current : borderBlocks)
			setColoredGlass(current.getLocation(), DyeColor.WHITE);
		
		for(Entity current : player.getNearbyEntities(4, 4, 4)) {
			if(current instanceof Player)
				((Player) current).teleport(testerLocation);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				endTesting(role);
				
			}
		}, TESTING_TIME * 20);
		
	}
	
	private void endTesting(Role role) {
		for(Block current : lamps) 
			setColoredGlass(current.getLocation(), (role == Role.INNOCENT) ? DyeColor.GREEN : DyeColor.RED);
		for(Block current : borderBlocks)
			world.getBlockAt(current.getLocation()).setType(Material.AIR);
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				resetTester();
			}
		}, TESTING_TIME * 20);
		
	}
	
	public void load() {
		for( int i = 0; i < borderBlocks.length; i++)
			borderBlocks[i] = new ConfigLocationUtil(plugin, "Arenas." + map.getName() + ".Tester.Borderblocks." + i).loadBlockLocation();
		for(int i = 0; i < lamps.length; i++)
			lamps[i] = new ConfigLocationUtil(plugin, "Arenas." + map.getName() + ".Tester.Lamps." + i).loadBlockLocation();
		
		button = new ConfigLocationUtil(plugin, "Arenas." + map.getName() + ".Tester.Button.").loadBlockLocation();
		testerLocation = new ConfigLocationUtil(plugin, "Arenas." + map.getName() + ".Tester.Location.").loadLocation();
		
		world = map.getSpectatorLocation().getWorld();
		resetTester();
	}
	
	private void resetTester() {
		inUse = false;
		
		for(Block current : borderBlocks)
			world.getBlockAt(current.getLocation()).setType(Material.AIR);
		for(Block current :lamps)
			setColoredGlass(current.getLocation(), DyeColor.BLACK);
	}
	
	private void setColoredGlass(Location location, DyeColor dyeColor) {
		Block block = world.getBlockAt(location);
		block.setType(Material.STAINED_GLASS);
		block.setData(dyeColor.getData());
	}
	
	public boolean exists() {
		return plugin.getConfig().getString("Arenas." + map.getName() + ".Tester.Location.World") != null;
	}
	
	public Block getButton() {
		return button;
	}

}

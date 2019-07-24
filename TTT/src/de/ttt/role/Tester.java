package de.ttt.role;

import org.bukkit.Location;
import org.bukkit.block.Block;

import de.ttt.main.TTT;
import de.ttt.utils.ConfigLocationUtil;
import de.ttt.voting.Map;

public class Tester {
	
	private TTT plugin;
	private Map map;
	
	private Block[] borderBlocks, lamps;
	private Block button;
	private Location testerLocation;
	private boolean inUse;
	
	public Tester(Map map, TTT plugin) {
		this.plugin = plugin;
		this.map = map;
		
		borderBlocks = new Block[3];
		lamps = new Block[2];
	}
	
	public void load() {
		for( int i = 0; i < borderBlocks.length; i++)
			borderBlocks[i] = new ConfigLocationUtil(plugin, "Arenas." + map.getName() + ".Tester.Borderblocks." + i).loadBlockLocation();
		for(int i = 0; i < lamps.length; i++)
			lamps[i] = new ConfigLocationUtil(plugin, "Arenas." + map.getName() + ".Tester.Lamps." + i).loadBlockLocation();
		
		button = new ConfigLocationUtil(plugin, "Arenas." + map.getName() + ".Tester.Button.").loadBlockLocation();
		testerLocation = new ConfigLocationUtil(plugin, "Arenas." + map.getName() + ".Tester.Location.").loadLocation();
	}
	
	public boolean exists() {
		return plugin.getConfig().isConfigurationSection("Arenas." + map.getName() + ".Tester");
	}

}

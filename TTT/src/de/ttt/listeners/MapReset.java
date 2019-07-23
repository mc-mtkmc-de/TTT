package de.ttt.listeners;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class MapReset implements Listener {
	private static List<String> CHANGES = new LinkedList<String>();
	
	public static void restore() {
		int blocks = 0;
		
		for(String b : CHANGES) {
			String[] blockdata = b.split(":");
			
			int id = Integer.parseInt(blockdata[0]);
			byte data = Byte.parseByte(blockdata[1]);
			World world = Bukkit.getWorld(blockdata[2]);
			int x = Integer.parseInt(blockdata[3]);
			int y = Integer.parseInt(blockdata[4]);
			int z = Integer.parseInt(blockdata[5]);
			
			world.getBlockAt(x, y, z).setTypeId(id);
			world.getBlockAt(x, y, z).setData(data);
			blocks++;
		}
		
		System.out.println("MapReset:" + blocks + "Blöcke wurden zurückgesetzt");
	}
	
	@EventHandler
	public void onBlock(BlockBreakEvent event) {
		Block b = event.getBlock();
		String block = b.getTypeId() + ":" + b.getData() + ":" + b.getWorld().getName() + ":" + b.getX() + ":" + b.getY() + ":" + b.getZ();
		
		CHANGES.add(block);
	}
	
	@EventHandler
	public void onBlock(BlockPlaceEvent event) {
		Block b = event.getBlock();
		String block = b.getTypeId() + ":" + b.getData() + ":" + b.getWorld().getName() + ":" + b.getX() + ":" + b.getY() + ":" + b.getZ();
		
		CHANGES.add(block);
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		for(int i = 0; i < event.blockList().size(); i++) {
			Block b = event.blockList().get(i);
			String block = b.getTypeId() + ":" + b.getData() + ":" + b.getWorld().getName() + ":" + b.getX() + ":" + b.getY() + ":" + b.getZ();
			
			CHANGES.add(block);
		}
	}


}

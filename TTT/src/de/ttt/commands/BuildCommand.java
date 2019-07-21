package de.ttt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ttt.gamestats.LobbyState;
import de.ttt.listeners.GameProtectionListener;
import de.ttt.main.TTT;

public class BuildCommand implements CommandExecutor {
	
	private TTT plugin;
	
	public BuildCommand(TTT plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("ttt.build")) {
				if(args.length == 0) {
					if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
						GameProtectionListener gameProtectionListener = plugin.getGameProtectionListener();
						if(!gameProtectionListener.getBuildModePlayers().contains(player.getName())) {
							gameProtectionListener.getBuildModePlayers().add(player.getName());
							player.sendMessage(TTT.PREFIX + "§aDu bist nun im §bBaumodus§a!");
						} else {
							gameProtectionListener.getBuildModePlayers().remove(player.getName());
							player.sendMessage(TTT.PREFIX + "§7Du bist nun nicht mehr im §bBaumodus&7!");
						}
					
					} else
						player.sendMessage(TTT.PREFIX + "§cDu kannst nur im §bLobbyState §cin den Baumodus!");
				} else
					player.sendMessage(TTT.PREFIX + "§cBitte benutze §b/build§c!");
			} else
				player.sendMessage(TTT.NO_PERMISSION);
		}
		
		return false;
	}

}

package de.ttt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ttt.main.TTT;
import de.ttt.utils.ConfigLocationUtil;

public class SetupCommand implements CommandExecutor{
	
	private TTT plugin;
	
	public SetupCommand(TTT plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("ttt.setup")) {
				if(args.length == 0) {
					player.sendMessage(TTT.PREFIX + "§cBitte benutze §b/setup <LOBBY>");
				} else {
					if(args[0].equalsIgnoreCase("lobby")) {
						if(args.length == 1) {
							new ConfigLocationUtil(plugin, player.getLocation(), "Lobby").saveLocation();
							player.sendMessage(TTT.PREFIX + "§aDie Lobby wurde neu gesetzt!");
						} else
							player.sendMessage(TTT.PREFIX + "§cBitte benutze §b/setup <lobby>");
					}
				}
			} else
				player.sendMessage(TTT.NO_PERMISSION);
		}
		return false;
	}
	
	

}

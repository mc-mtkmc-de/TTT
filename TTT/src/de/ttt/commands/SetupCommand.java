package de.ttt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ttt.gamestats.LobbyState;
import de.ttt.main.TTT;
import de.ttt.utils.ConfigLocationUtil;
import de.ttt.voting.Map;

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
				
				// /setup <lobby>
				
				if(args.length == 0) {
					player.sendMessage(TTT.PREFIX + "§cBitte benutze §b/setup <LOBBY>");
				} else {
					if(args[0].equalsIgnoreCase("lobby")) {
						if(args.length == 1) {
							new ConfigLocationUtil(plugin, player.getLocation(), "Lobby").saveLocation();
							player.sendMessage(TTT.PREFIX + "§aDie Lobby wurde neu gesetzt!");
						} else
							player.sendMessage(TTT.PREFIX + "§cBitte benutze §b/setup <lobby>");
						
						// /setup create <NAME> <ERBAUER>
						
					} else if(args[0].equalsIgnoreCase("create")) {
						if(args.length == 3) {
							Map map = new Map(plugin, args[1]);
							if(!map.exists()) {
								map.create(args[2]);
								player.sendMessage(TTT.PREFIX + "§aDie Map §6" + map.getName() + " §awurde erstellt!");
							} else
								player.sendMessage(TTT.PREFIX + "§cDiese Map existiert bereits!");
						} else
							player.sendMessage(TTT.PREFIX + "§cBitte benutze §b/setup create <NAME> <ERBAUER>§c!");
						
						// /setup set <NAME> <SPIELERANZAHL / SPECTATOR>
						
					} else if(args[0].equalsIgnoreCase("set")) {
						if(args.length == 3) {
							Map map = new Map(plugin, args[1]);
							if(map.exists()) {
								try {
									int spawnNumber = Integer.parseInt(args[2]);
									if(spawnNumber > 0 && spawnNumber <= LobbyState.MAX_PLAYERS) {
										map.setSpawnLocation(spawnNumber, player.getLocation());
										player.sendMessage(TTT.PREFIX + "§aDu hast die Spawn-Location §b" + spawnNumber + "§a für die Map §b" + map.getName() + " §agesetzt!");
									} else
										player.sendMessage(TTT.PREFIX + "§cBitte gib eine Zahl §bzwischen 1 und " + LobbyState.MAX_PLAYERS + " §can!");
								} catch(NumberFormatException e) {
									if(args[2].equalsIgnoreCase("spectator")) {
										map.setSpectatorLocation(player.getLocation());
										player.sendMessage(TTT.PREFIX + "§aDu hast die Spectator-Location für die Map §b" + map.getName() + " §agesetzt!");
									} else
										player.sendMessage(TTT.PREFIX + "§C Bitte benutze §b /setup set <NAME> >1-" + LobbyState.MAX_PLAYERS + " // SPECTATOR>");
								}
								
							} else
								player.sendMessage(TTT.PREFIX + "§cDiese Map existiert noch nicht.");
						} else
							player.sendMessage(TTT.PREFIX + "§C Bitte benutze §b /setup set <NAME> >1-" + LobbyState.MAX_PLAYERS + " // SPECTATOR>");
					}
				}
			} else
				player.sendMessage(TTT.NO_PERMISSION);
		}
		return false;
	}
	
	

}

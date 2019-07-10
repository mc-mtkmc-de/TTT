package de.ttt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ttt.gamestats.LobbyState;
import de.ttt.main.TTT;

public class StartCommand implements CommandExecutor {
	
	private static final int START_SECONDS = 5;
	
	private TTT plugin;
	
	public StartCommand(TTT plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("ttt.start")) {
				if(args.length == 0) {
					if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
						LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
						if(lobbyState.getCountdown().isRunning() && (lobbyState.getCountdown().getSeconds() > START_SECONDS)) {
							lobbyState.getCountdown().setSeconds(START_SECONDS);
							player.sendMessage(TTT.PREFIX + "§aDer Spielstart wurde beschleunigt!");
						} else
							player.sendMessage(TTT.PREFIX + "§cDas Spiel ist bereits gestartet!");
					} else
						player.sendMessage(TTT.PREFIX + "§cDas Spiel ist bereits gestartet!");
				} else
					player.sendMessage(TTT.PREFIX + "§cBitte benutze §b/start");
			} else
				player.sendMessage(TTT.NO_PERMISSION);
		}
		return false;
	}
	

}

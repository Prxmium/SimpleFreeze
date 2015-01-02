package io.github.Prxmium.SimpleFreeze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleFreeze extends JavaPlugin implements Listener {

	public ArrayList<String> frozenPlayers = new ArrayList<String>();

	private File folderFile = new File("plugins/SimplyFreeze");
	private File dataFile = new File("plugins/SimplyFreeze/data.txt");

	private boolean warn = false;

	public void onEnable() {
		if (!folderFile.exists() || !dataFile.exists()) {
			try {
				folderFile.mkdirs();
				dataFile.createNewFile();
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Error while creating files.");
			}
		} else {
			try (BufferedReader reader = new BufferedReader(new FileReader(
					dataFile))) {
				String line = reader.readLine();
				warn = Boolean.valueOf(line);
			} catch (IOException e) {
				getLogger().log(Level.SEVERE,
						"Error while reading from file or invalid value.");
			}
		}

		frozenPlayers.clear();
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("freeze")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Usage: /freeze <Player>");
			} else {
				switch (args.length) {
				case 1:
					Player p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						sender.sendMessage(ChatColor.RED
								+ "That player is not online!");
					} else {
						if (frozenPlayers.contains(p.getName())) {
							frozenPlayers.remove(p.getName());
							p.sendMessage(ChatColor.AQUA
									+ "You have been unfrozen!");
						} else {
							frozenPlayers.add(p.getName());
							p.sendMessage(ChatColor.AQUA
									+ "You were frozen by " + sender.getName()
									+ "!");
						}
					}
					break;
				}
			}
		}

		if (commandLabel.equalsIgnoreCase("warn") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 0) {
				warn = Boolean.valueOf(args[0]);
				try (FileWriter writer = new FileWriter(dataFile)) {
					writer.write(args[0]);
				} catch (IOException e) {
					getLogger().log(Level.SEVERE,
							"Error while writing to file.");
				}
				player.sendMessage(ChatColor.AQUA + "Warn is set to"
						+ Boolean.toString(warn));

				return true;
			} else {
				player.sendMessage("Wrong syntax!");
				return true;
			}
		}

		return false;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (frozenPlayers.contains(e.getPlayer().getName())) {
			e.getPlayer().teleport(e.getPlayer());
			if (warn) {
				e.getPlayer().sendMessage(ChatColor.AQUA + "You are frozen!");
			}
		} else {

		}
	}
}

package main;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	FileHandler fileHandler = new FileHandler("plugins/SimpleFreeze");

	Config config = new Config();

	public void onEnable()
	{
		config.validate();

		if (!DataHandler.validate()) DataHandler.createFile();

		getServer().getPluginManager().registerEvents(this, this);

		getLogger().info("Successfully enabled " + getName() + "!");
	}

	public void onDisable()
	{
		DataHandler.savePlayerData(frozenPlayers);

		getLogger().info("Successfully disabled " + getName() + "!");
	}

	private HashMap<String, Location> frozenPlayers = new HashMap<>();

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (label.equalsIgnoreCase("freeze"))
		{
			if (args.length > 0)
			{
				Player player = Bukkit.getPlayer(args[0]);

				String playerName = player.getName();

				if (frozenPlayers.containsKey(playerName))
				{

					frozenPlayers.remove(playerName);

					player.sendMessage(ChatColor.DARK_AQUA + "You've been unfrozen!");

					sender.sendMessage(ChatColor.DARK_AQUA + "You've unfrozen " + playerName + "!");
				}

				Location location = player.getLocation();

				frozenPlayers.put(playerName, location);

				String x = Double.toString(location.getX());
				String y = Double.toString(location.getY());
				String z = Double.toString(location.getZ());

				DataHandler.writeDataSet(playerName, x, y, z);

				player.sendMessage(ChatColor.DARK_AQUA + "You've been frozen!");

				sender.sendMessage(ChatColor.DARK_AQUA + "You've frozen " + playerName + "!");

				if (Boolean.valueOf(config.getValueOf("kick-on-freeze"))) player.kickPlayer("You've been frozen.");

				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.DARK_RED + "Wrong syntax!");

				return false;
			}
		}
		if (label.equalsIgnoreCase("frozenplayers"))
		{
			if (frozenPlayers.isEmpty()) sender.sendMessage(ChatColor.RED + "Nobody is frozen!");
			else if (!frozenPlayers.isEmpty())
			{
				sender.sendMessage(ChatColor.DARK_AQUA + "Frozen players:");

				Set<String> keySet = frozenPlayers.keySet();

				for (String key : keySet)
				{
					sender.sendMessage(ChatColor.DARK_AQUA + "- " + key);
				}
			}

			return true;
		}

		return false;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();

		if (Boolean.valueOf(config.getValueOf("mute-on-freeze")) && frozenPlayers.containsKey(player.getName()))
		{
			player.sendMessage(ChatColor.DARK_AQUA + "You're frozen and can't speak!");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();

		String playerName = player.getName();

		if (frozenPlayers.containsKey(playerName))
		{
			player.teleport(frozenPlayers.get(playerName));
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		DataHandler.loadDataFromFile(event.getPlayer(), frozenPlayers);
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		String playerName = player.getName();

		if (frozenPlayers.containsKey(playerName))
		{
			Location location = frozenPlayers.get(playerName);

			String x = Double.toString(location.getX());
			String y = Double.toString(location.getY());
			String z = Double.toString(location.getZ());

			DataHandler.writeDataSet(playerName, x, y, z);
		}
	}
}

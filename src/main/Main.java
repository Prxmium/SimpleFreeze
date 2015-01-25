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
	PluginFile pluginFile = new PluginFile();
	ConfigFile configFile = new ConfigFile();

	public void onEnable()
	{
		pluginFile.validate();
		configFile.validate();

		getServer().getPluginManager().registerEvents(this, this);

		getLogger().info("Enabled " + getName() + ".");
	}

	public void onDisable()
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			String playerName = player.getName();

			if (!frozenPlayers.containsKey(playerName)) DataHandler.deletePlayer(playerName);
			else DataHandler.savePlayer(playerName, player.getLocation());
		}

		getLogger().info("Disabled " + getName() + ".");
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

					player.sendMessage(configFile.getValueOf(ConfigKey.UNFREEZE_MESSAGE));

					sender.sendMessage(playerName + " is no longer frozen.");

					return true;
				}

				Location location = player.getLocation();

				frozenPlayers.put(playerName, location);

				player.sendMessage(configFile.getValueOf(ConfigKey.FREEZE_MESSAGE));

				sender.sendMessage(playerName + " has been frozen.");

				if (Boolean.valueOf(configFile.getValueOf(ConfigKey.KICK_ON_FREEZE))) player.kickPlayer(configFile.getValueOf(ConfigKey.KICK_MESSAGE));

				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.DARK_RED + "Wrong syntax.");

				return false;
			}
		}
		if (label.equalsIgnoreCase("frozenplayers"))
		{
			if (frozenPlayers.isEmpty()) sender.sendMessage(ChatColor.RED + "There are no frozen players.");
			else if (!frozenPlayers.isEmpty())
			{
				sender.sendMessage("Frozen players:");

				Set<String> keySet = frozenPlayers.keySet();

				for (String key : keySet)
				{
					sender.sendMessage("- " + key);
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

		if (Boolean.valueOf(configFile.getValueOf(ConfigKey.MUTE_ON_FREEZE)) && frozenPlayers.containsKey(player.getName()))
		{
			player.sendMessage(configFile.getValueOf(ConfigKey.MUTE_MESSAGE));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();

		String playerName = player.getName();

		if (frozenPlayers.containsKey(playerName)) player.teleport(frozenPlayers.get(playerName));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		String playerName = player.getName();

		DataHandler.createPlayer(playerName);

		DataHandler.loadPlayer(playerName, frozenPlayers);
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		String playerName = player.getName();

		if (frozenPlayers.containsKey(playerName)) DataHandler.savePlayer(playerName, player.getLocation());
		else DataHandler.deletePlayer(playerName);
	}
}

package prxmium.simplefreeze;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	private final DataHandler dataHandler = new DataHandler();

	public void onEnable()
	{
		dataHandler.exists();

		getServer().getPluginManager().registerEvents(this, this);

		getLogger().info("Successfully enabled " + getName() + "!");
	}

	public void onDisable()
	{
		dataHandler.savePlayerData(frozenPlayers);

		getLogger().info("Successfully disabled " + getName() + "!");
	}

	private HashMap<String, Location> frozenPlayers = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("freeze"))
		{
			if (args.length > 0)
			{
				@SuppressWarnings("deprecation")
				Player player = Bukkit.getPlayer(args[0]);

				String playerName = player.getName();

				Location location = player.getLocation();

				frozenPlayers.put(playerName, location);

				String x = Double.toString(location.getX());
				String y = Double.toString(location.getY());
				String z = Double.toString(location.getZ());

				dataHandler.writeDataSet(playerName, x, y, z);

				player.sendMessage(ChatColor.DARK_AQUA + "You've been frozen!");

				sender.sendMessage(ChatColor.DARK_AQUA + "You've frozen " + playerName + "!");

				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.DARK_RED + "Wrong syntax!");

				return false;
			}
		}
		if (command.getName().equalsIgnoreCase("unfreeze"))
		{
			if (args.length > 0)
			{
				@SuppressWarnings("deprecation")
				Player player = Bukkit.getPlayer(args[0]);

				String playerName = player.getName();

				frozenPlayers.remove(playerName);

				player.sendMessage(ChatColor.DARK_AQUA + "You've been unfrozen!");

				sender.sendMessage(ChatColor.DARK_AQUA + "You've unfrozen " + playerName + "!");

				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.DARK_RED + "Wrong syntax!");

				return false;
			}
		}

		return false;
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
		dataHandler.loadDataFromFile(event.getPlayer(), frozenPlayers);
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

			dataHandler.writeDataSet(playerName, x, y, z);
		}
	}
}

package prxmium.simplefreeze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

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
	private File folderFile = new File("plugins/SimpleFreeze");
	private File dataFile = new File("plugins/SimpleFreeze/data.txt");

	private final String delimiter = ";";

	public void onEnable()
	{
		exists();

		getServer().getPluginManager().registerEvents(this, this);

		getLogger().info("Successfully enabled " + getName() + "!");
	}

	public void onDisable()
	{
		savePlayerData();

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

				writeDataSet(playerName + delimiter + x + delimiter + y + delimiter + z + delimiter);

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
		loadDataFromFile(event.getPlayer());
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		String playerName = player.getName();

		if (frozenPlayers.containsKey(playerName))
		{
			try (PrintWriter writer = new PrintWriter(dataFile))
			{
				Location location = frozenPlayers.get(playerName);

				String x = Double.toString(location.getX());
				String y = Double.toString(location.getY());
				String z = Double.toString(location.getZ());

				writer.println(playerName + delimiter + x + delimiter + y + delimiter + z + delimiter);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean exists()
	{
		if (!folderFile.exists() || !dataFile.exists())
		{
			folderFile.mkdirs();

			try
			{
				dataFile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return false;
		}

		return true;
	}

	public void loadDataFromFile(Player player)
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(dataFile)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				ArrayList<String> data = readDataSet(line);

				if (player.getName().equals(data.get(0)))
				{
					double x = Double.parseDouble(data.get(1));
					double y = Double.parseDouble(data.get(2));
					double z = Double.parseDouble(data.get(3));

					Location location = new Location(player.getWorld(), x, y, z);

					if (!frozenPlayers.containsKey(player.getName()))
					{
						frozenPlayers.put(player.getName(), location);
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void writeDataSet(String dataSet)
	{
		try (PrintWriter writer = new PrintWriter(dataFile))
		{
			writer.println(dataSet);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void savePlayerData()
	{
		try (PrintWriter writer = new PrintWriter(dataFile))
		{
			Set<String> keys = frozenPlayers.keySet();

			for (String key : keys)
			{
				@SuppressWarnings("deprecation")
				Player player = Bukkit.getPlayer(key);

				Location location = player.getLocation();

				String x = Double.toString(location.getX());
				String y = Double.toString(location.getY());
				String z = Double.toString(location.getZ());

				writer.println(key + delimiter + x + delimiter + y + delimiter + z + delimiter);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<String> readDataSet(String dataSet)
	{
		ArrayList<String> data = new ArrayList<>();

		try (Scanner scanner = new Scanner(dataSet))
		{
			scanner.useDelimiter(delimiter);

			while (scanner.hasNext())
			{
				data.add(scanner.next());
			}
		}

		return data;
	}
}

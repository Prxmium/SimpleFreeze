package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DataHandler
{
	public static void createPlayer(String playerName)
	{
		File file = new File("plugins/SimpleFreeze/playerdata/" + playerName + ".txt");

		if (!file.exists()) try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void deletePlayer(String playerName)
	{
		try
		{
			Files.deleteIfExists(Paths.get("plugins/SimpleFreeze/playerdata/" + playerName + ".txt"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void savePlayer(String playerName, Location location)
	{
		String x = Double.toString(location.getX());
		String y = Double.toString(location.getY());
		String z = Double.toString(location.getZ());

		try
		{
			Files.write(Paths.get("plugins/SimpleFreeze/playerdata/" + playerName + ".txt"), String.format("%s;%s;%s;", x, y, z).getBytes(), StandardOpenOption.WRITE);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void loadPlayer(String playerName, HashMap<String, Location> frozenPlayers)
	{
		try (Scanner scanner = new Scanner(Paths.get("plugins/SimpleFreeze/playerdata/" + playerName + ".txt")))
		{
			scanner.useDelimiter(";");

			if (!scanner.hasNext()) return;

			double x = Double.parseDouble(scanner.next());
			double y = Double.parseDouble(scanner.next());
			double z = Double.parseDouble(scanner.next());

			if (!frozenPlayers.containsKey(playerName)) frozenPlayers.put(playerName, new Location(Bukkit.getPlayer(playerName).getWorld(), x, y, z));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

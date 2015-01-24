package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DataFile extends File
{
	private static final long serialVersionUID = 1L;

	public DataFile()
	{
		super("plugins/SimpleFreeze/data.txt");
	}

	public void validate()
	{
		if (!exists())
		{
			try
			{
				createNewFile();
			}
			catch (IOException e)
			{
				Bukkit.getLogger().log(Level.WARNING, "Error while creating config.", e);
			}
		}
	}

	public void savePlayerData(HashMap<String, Location> frozenPlayers)
	{
		try (PrintWriter writer = new PrintWriter(this))
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

				writer.println(String.format("%s;%s;%s;%s;", key, x, y, z));
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void loadDataFromFile(Player player, HashMap<String, Location> frozenPlayers)
	{
		try (BufferedReader reader = Files.newBufferedReader(this.toPath(), Charset.defaultCharset()))
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

	public void writeDataSet(String val1, String val2, String val3, String val4)
	{
		String set = String.format("%s;%s;%s;%s;", val1, val2, val3, val4);

		try (PrintWriter writer = new PrintWriter(this))
		{
			writer.println(set);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static ArrayList<String> readDataSet(String dataSet)
	{
		ArrayList<String> data = new ArrayList<>();

		try (Scanner scanner = new Scanner(dataSet))
		{
			scanner.useDelimiter(";");

			while (scanner.hasNext())
			{
				data.add(scanner.next());
			}
		}

		return data;
	}
}

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
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DataHandler
{
	private File folderFile = new File("plugins/SimpleFreeze");
	private File dataFile = new File("plugins/SimpleFreeze/data.txt");

	private final String delimiter = ";";

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

	public void savePlayerData(HashMap<String, Location> frozenPlayers)
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

	public void loadDataFromFile(Player player, HashMap<String, Location> frozenPlayers)
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

	public void writeDataSet(String val1, String val2, String val3, String val4)
	{
		String dataSet = val1 + delimiter + val2 + delimiter + val3 + delimiter + val4 + delimiter;

		try (PrintWriter writer = new PrintWriter(dataFile))
		{
			writer.println(dataSet);
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

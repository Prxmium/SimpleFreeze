package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigHandler
{
	private static final String directory = "plugins/SimpleFreeze/config.txt";
	private static final String delimiter = ":";

	private static final File configFile = new File(directory);

	public static final ConfigKey MUTE_ON_FREEZE = new ConfigKey("mute-on-freeze", "false");
	public static final ConfigKey KICK_ON_FREEZE = new ConfigKey("kick-on-freeze", "false");

	private static final ConfigKey[] configKeys =
	{ MUTE_ON_FREEZE, KICK_ON_FREEZE };

	public static boolean validate()
	{
		if (!configFile.exists()) return false;
		else return true;
	}

	public static void createConfig()
	{
		try
		{
			configFile.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		addKeys();
	}

	public static void loadKeys()
	{
		ArrayList<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(configFile)))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				lines.add(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		for (int i = 0; i < lines.size(); i++)
		{
			try (Scanner scanner = new Scanner(lines.get(i)))
			{
				scanner.useDelimiter(delimiter);

				if (configKeys[i].getKey().equals(scanner.next()))
				{
					configKeys[i].setValue(scanner.next());
				}
			}
		}
	}

	private static void addKeys()
	{
		try (PrintWriter writer = new PrintWriter(configFile))
		{
			for (int i = 0; i < configKeys.length; i++)
			{
				writer.println(configKeys[i].getKey() + delimiter + configKeys[i].getValue());
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}

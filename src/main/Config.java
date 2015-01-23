package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class Config
{
	private String delimiter = ":";

	private String[][] options =
	{
	{ "mute-on-freeze", "false" },
	{ "kick-on-freeze", "false" } };

	private File configFile = new File("plugins/SimpleFreeze/config.txt");

	public void validate()
	{
		if (configFile.exists() && configFile.length() != 0)
		{
			loadOptions();

			return;
		}

		try
		{
			configFile.createNewFile();
		}
		catch (IOException e)
		{
			Bukkit.getLogger().log(Level.WARNING, "Error while creating config.", e);
		}

		try (PrintWriter writer = new PrintWriter(configFile))
		{
			for (int i = 0; i < options.length; i++)
			{
				writer.println(options[i][0] + delimiter + options[i][1]);
			}
		}
		catch (FileNotFoundException e)
		{
			Bukkit.getLogger().log(Level.WARNING, "Error while writing to config.", e);
		}

		loadOptions();
	}

	public void loadOptions()
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

				if (options[i][0].equals(scanner.next())) options[i][1] = scanner.next();
			}
		}
	}

	public int getLength()
	{
		int lineCount = 0;

		try (Scanner scanner = new Scanner(configFile))
		{
			while (scanner.hasNextLine())
			{
				lineCount++;

				scanner.nextLine();
			}
		}
		catch (IOException e)
		{
			Bukkit.getLogger().log(Level.WARNING, "Error while getting config length.", e);
		}

		return lineCount;
	}

	public String getValueOf(String key)
	{
		for (int i = 0; i < options.length; i++)
		{
			if (options[i][0].equals(key)) return options[i][1];
		}

		return null;
	}
}

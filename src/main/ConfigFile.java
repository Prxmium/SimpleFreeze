package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class ConfigFile extends File
{
	private static final long serialVersionUID = 1L;

	private String[][] keys =
	{
	{ ConfigKey.MUTE_ON_FREEZE, "false" },
	{ ConfigKey.KICK_ON_FREEZE, "false" },
	{ ConfigKey.MUTE_MESSAGE, "You are frozen and cannot speak." },
	{ ConfigKey.KICK_MESSAGE, "You have been frozen." },
	{ ConfigKey.FREEZE_MESSAGE, "You have been frozen." },
	{ ConfigKey.UNFREEZE_MESSAGE, "You have been unfrozen." } };

	public ConfigFile()
	{
		super("plugins/SimpleFreeze/config.txt");
	}

	public void validate()
	{
		if (exists() && length() != 0)
		{
			loadOptions();

			return;
		}

		try
		{
			createNewFile();
		}
		catch (IOException e)
		{
			Bukkit.getLogger().log(Level.WARNING, "Error while creating config.", e);
		}

		try (PrintWriter writer = new PrintWriter(this))
		{
			for (int i = 0; i < keys.length; i++)
			{
				writer.println(String.format("%s: %s", keys[i][0], keys[i][1]));
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

		try (BufferedReader reader = Files.newBufferedReader(this.toPath(), Charset.defaultCharset()))
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
				scanner.useDelimiter(":");

				if (keys[i][0].equals(scanner.next().trim())) keys[i][1] = scanner.next().trim();
			}
		}
	}

	public int getLength()
	{
		int lineCount = 0;

		try (Scanner scanner = new Scanner(this))
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
		for (int i = 0; i < keys.length; i++)
		{
			if (keys[i][0].equalsIgnoreCase(key)) return keys[i][1];
		}

		return null;
	}
}

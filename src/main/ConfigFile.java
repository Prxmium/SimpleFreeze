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

	private String[][] options =
	{
	{ "mute-on-freeze", "false" },
	{ "kick-on-freeze", "false" },
	{ "mute-message", "You are frozen and cannot speak." },
	{ "kick-message", "You have been frozen." } };

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
			for (int i = 0; i < options.length; i++)
			{
				writer.println(String.format("%s: %s", options[i][0], options[i][1]));
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

				if (options[i][0].equals(scanner.next().trim())) options[i][1] = scanner.next().trim();
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
		for (int i = 0; i < options.length; i++)
		{
			if (options[i][0].equalsIgnoreCase(key)) return options[i][1];
		}

		return null;
	}
}

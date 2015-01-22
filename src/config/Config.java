package config;

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

	private File configFile;

	private ArrayList<ConfigOption> configOptions;

	public Config(String directory, String fileName, String fileExtension)
	{
		configFile = new File(directory + fileName + fileExtension);

		configOptions = new ArrayList<>();
	}

	public void validate()
	{
		if (configFile.exists())
		{
			for (int i = 0; i < options.length; i++)
			{
				configOptions.add(new ConfigOption(options[i][0], options[i][1]));
			}

			loadOptions();

			return;
		}
		else
		{
			try
			{
				configFile.createNewFile();
			}
			catch (IOException e)
			{
				Bukkit.getLogger().log(Level.WARNING, "Error while creating config.", e);
			}
		}

		int configLength = getLength();

		if (configLength != configOptions.size()) return;

		try (PrintWriter writer = new PrintWriter(configFile))
		{
			for (int i = 0; i < configOptions.size(); i++)
			{
				writer.println(configOptions.get(i).getKey() + delimiter + configOptions.get(i).getValue());
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

				if (configOptions.get(i).getKey().equals(scanner.next()))
				{
					configOptions.get(i).setValue(scanner.next());
				}
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
			Bukkit.getLogger().log(Level.WARNING, null, e);
		}

		return lineCount;
	}

	public String getValue(String key)
	{
		for (int i = 0; i < configOptions.size(); i++)
		{
			if (configOptions.get(i).getKey().equals(key)) return configOptions.get(i).getValue();
		}
		return null;
	}
}

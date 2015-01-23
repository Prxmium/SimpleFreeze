package main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class PluginFile extends File
{
	private static final long serialVersionUID = 1L;

	public PluginFile()
	{
		super("plugins/SimpleFreeze");
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
				Bukkit.getLogger().log(Level.WARNING, "Couldn't create plugin folder.", e);
			}
		}
	}
}

package main;

import java.io.File;

import config.ConfigHandler;

public class FileHandler
{
	private File pluginFolder;

	public FileHandler(String directory)
	{
		pluginFolder = new File(directory);

		if (!pluginFolder.exists()) pluginFolder.mkdirs();
	}

	public void validateFiles()
	{
		if (ConfigHandler.validate() && DataHandler.validate()) return;
		else
		{
			ConfigHandler.createConfig();
			DataHandler.createFile();
		}
	}
}

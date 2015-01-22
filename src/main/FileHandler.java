package main;

import java.io.File;

public class FileHandler
{
	private File pluginFolder;

	public FileHandler(String directory)
	{
		pluginFolder = new File(directory);

		if (!pluginFolder.exists()) pluginFolder.mkdirs();
	}
}

package main;

import java.io.File;

public class PluginFile extends File
{
	private static final long serialVersionUID = 1L;

	public PluginFile()
	{
		super("plugins/SimpleFreeze");

		createSubDirectories();
	}

	public void validate()
	{
		if (!exists())
		{
			mkdirs();
		}
	}

	private void createSubDirectories()
	{
		File dataFolder = new File(getPath() + "/playerdata");

		if (!dataFolder.exists()) dataFolder.mkdirs();
	}
}

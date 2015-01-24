package main;

import java.io.File;

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
			mkdirs();	
		}
	}
}

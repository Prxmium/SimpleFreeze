package prxmium.simplefreeze;

import java.io.File;

public class FileHandler
{
	private static final File pluginFolder = new File("plugins/SimpleFreeze");

	public static boolean validateFiles()
	{
		if (pluginFolder.exists() && ConfigHandler.validate() && DataHandler.validate()) return true;
		else return false;
	}

	public static void createFolder()
	{
		pluginFolder.mkdirs();
	}
}

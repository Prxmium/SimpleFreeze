package prxmium.simplefreeze;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);

		getLogger().info(getName() + " has been enabled successfully.");
	}

	public void onDisable()
	{
		getLogger().info(getName() + " has been disabled successfully.");
	}

	private HashMap<Player, Location> frozenPlayers = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("freeze"))
		{
			if (args.length > 0)
			{
				@SuppressWarnings("deprecation")
				Player player = Bukkit.getPlayer(args[0]);

				frozenPlayers.put(player, player.getLocation());

				player.sendMessage(ChatColor.DARK_AQUA + "You've been frozen!");

				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Wrong syntax!");

				return false;
			}
		}
		if (command.getName().equalsIgnoreCase("unfreeze"))
		{
			if (args.length > 0)
			{
				@SuppressWarnings("deprecation")
				Player player = Bukkit.getPlayer(args[0]);

				frozenPlayers.remove(player);

				player.sendMessage(ChatColor.DARK_AQUA + "You've been unfrozen!");
				
				return true;
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Wrong syntax!");
				
				return false;
			}
		}

		return false;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();

		if (frozenPlayers.containsKey(player))
		{
			player.teleport(frozenPlayers.get(player));
		}
	}
}

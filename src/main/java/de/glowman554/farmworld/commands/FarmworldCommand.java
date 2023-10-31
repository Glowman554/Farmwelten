package de.glowman554.farmworld.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.glowman554.farmworld.FarmWorldMain;
import de.glowman554.farmworld.gui.FarmworldGUI;

public class FarmworldCommand implements CommandExecutor
{
	private FarmworldGUI gui = new FarmworldGUI();

	public FarmworldCommand()
	{
		Bukkit.getServer().getPluginManager().registerEvents(gui, FarmWorldMain.getInstance());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if ((sender instanceof Player))
		{
			// TODO
		}
		Player player = (Player) sender;
		try
		{
			gui.openInventoryForPlayer(player);
		}
		catch (SQLException e)
		{
			FarmWorldMain.getInstance().genericError(player);
		}
		return false;
	}

}

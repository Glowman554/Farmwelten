package de.glowman554.farmworld.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.glowman554.farmworld.FarmWorldMain;
import de.glowman554.farmworld.gui.FarmworldMainGUI;

public class FarmworldCommand implements CommandExecutor
{
	private FarmworldMainGUI gui = new FarmworldMainGUI();

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

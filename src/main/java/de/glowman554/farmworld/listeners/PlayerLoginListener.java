package de.glowman554.farmworld.listeners;

import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import de.glowman554.farmworld.FarmWorldMain;
import de.glowman554.farmworld.WorldId;

public class PlayerLoginListener implements Listener
{
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e)
	{
		try
		{
			for (WorldId id : WorldId.values())
			{
				FarmWorldMain.getInstance().getDatabase().createUserWorldIfNecesarry(e.getPlayer().getUniqueId(), id);
			}
		}
		catch (SQLException err)
		{
			err.printStackTrace();
			throw new IllegalStateException(err);
		}
	}
}

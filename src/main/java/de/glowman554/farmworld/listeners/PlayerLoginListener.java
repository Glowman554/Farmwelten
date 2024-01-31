package de.glowman554.farmworld.listeners;

import de.glowman554.farmworld.FarmWorldMain;
import de.glowman554.farmworld.WorldId;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.SQLException;

public class PlayerLoginListener implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        try {
            for (WorldId id : WorldId.values()) {
                FarmWorldMain.getInstance().getDatabase().createUserWorldIfNecessary(e.getPlayer().getUniqueId(), id);
            }
        } catch (SQLException err) {
            err.printStackTrace();
            throw new IllegalStateException(err);
        }
    }
}

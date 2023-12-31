package de.glowman554.farmworld.db;

import java.sql.SQLException;
import java.util.UUID;

import de.glowman554.farmworld.WorldId;

public interface DatabaseConnection
{
	public void close();
	public void createUserWorldIfNecesarry(UUID uuid, WorldId id) throws SQLException;
	public void changeUserWorldLevel(UUID uuid, WorldId id, int newLevel) throws SQLException;
	public int readUserWorldLevel(UUID uuid, WorldId id) throws SQLException;
	public void scheduleUserTeleport(UUID uuid, WorldId id, int level) throws SQLException;
	public void scheduleUserTeleport(UUID uuid, String id) throws SQLException;
}

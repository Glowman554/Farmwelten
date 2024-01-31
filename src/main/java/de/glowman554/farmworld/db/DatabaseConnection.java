package de.glowman554.farmworld.db;

import de.glowman554.farmworld.WorldId;

import java.sql.SQLException;
import java.util.UUID;

public interface DatabaseConnection {
    void close();

    void createUserWorldIfNecessary(UUID uuid, WorldId id) throws SQLException;

    void changeUserWorldLevel(UUID uuid, WorldId id, int newLevel) throws SQLException;

    int readUserWorldLevel(UUID uuid, WorldId id) throws SQLException;

    void scheduleUserTeleport(UUID uuid, WorldId id, int level) throws SQLException;

    void scheduleUserTeleport(UUID uuid, String id) throws SQLException;
}

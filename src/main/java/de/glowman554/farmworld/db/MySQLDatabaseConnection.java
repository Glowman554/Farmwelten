package de.glowman554.farmworld.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.yaml.snakeyaml.util.UriEncoder;

import de.glowman554.farmworld.WorldId;
import de.glowman554.farmworld.utils.FileUtils;

public class MySQLDatabaseConnection implements DatabaseConnection
{
	private Connection connect = null;

	public MySQLDatabaseConnection(String url, String database, String username, String password) throws ClassNotFoundException, SQLException, IOException
	{
		connect = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s&autoReconnect=true", url, database, username, UriEncoder.encode(password)));

		execute_script("database_setup");
	}

	public void close()
	{
		try
		{
			connect.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private void execute_script(String script_name) throws SQLException, IOException
	{
		Statement s = connect.createStatement();
		String[] sql_commands = FileUtils.readFile(this.getClass().getResourceAsStream("/sql/" + script_name + ".sql")).split(";");

		for (String sql : sql_commands)
		{
			sql = sql.trim();
			if (sql.equals(""))
			{
				continue;
			}

			s.execute(sql);
		}

		s.close();
	}

	public void createUserWorldIfNecesarry(UUID uuid, WorldId id) throws SQLException
	{
		PreparedStatement st = connect.prepareStatement("insert ignore worldLevels (uuid, worldLevel, worldId) values (?, ?, ?)");
		st.setString(1, uuid.toString());
		st.setInt(2, 1);
		st.setString(3, id.toString());
		st.execute();
		st.close();
	}

	public void changeUserWorldLevel(UUID uuid, WorldId id, int newLevel) throws SQLException
	{
		PreparedStatement st = connect.prepareStatement("update worldLevels set worldLevel = ? where uuid = ? and worldId = ?");
		st.setInt(1, newLevel);
		st.setString(2, uuid.toString());
		st.setString(3, id.toString());
		st.execute();
		st.close();
	}

	public int readUserWorldLevel(UUID uuid, WorldId id) throws SQLException
	{
		PreparedStatement st = connect.prepareStatement("select worldLevel from worldLevels where uuid = ? and worldId = ?");
		st.setString(1, uuid.toString());
		st.setString(2, id.toString());

		ResultSet rs = st.executeQuery();
		if (rs.next())
		{
			int result = rs.getInt("worldLevel");
			rs.close();
			st.close();
			return result;
		}
		else
		{
			rs.close();
			st.close();
			throw new IllegalArgumentException("Player in database not found!");
		}
	}

	@Override
	public void scheduleUserTeleport(UUID uuid, WorldId id, int level) throws SQLException {
		PreparedStatement st = connect.prepareStatement("replace into scheduledTeleports (uuid, worldLevel, worldId) values (?, ?, ?)");
		st.setString(1, uuid.toString());
		st.setInt(2, level);
		st.setString(3, id.toString());
		st.execute();
		st.close();
	}

	@Override
	public void scheduleUserTeleport(UUID uuid, String id) throws SQLException {
		PreparedStatement st = connect.prepareStatement("replace into scheduledTeleports (uuid, worldLevel, worldId) values (?, -1, ?)");
		st.setString(1, uuid.toString());
		st.setString(2, id);
		st.execute();
		st.close();
	}
}

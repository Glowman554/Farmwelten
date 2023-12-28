package de.glowman554.farmworld;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.glowman554.farmworld.commands.FarmworldCommand;
import de.glowman554.farmworld.db.DatabaseConnection;
import de.glowman554.farmworld.db.MySQLDatabaseConnection;
import de.glowman554.farmworld.db.SQLiteDatabaseConnection;
import de.glowman554.farmworld.listeners.PlayerLoginListener;
import net.milkbowl.vault.economy.Economy;

public class FarmWorldMain extends JavaPlugin
{
	private static FarmWorldMain instance;

	public FarmWorldMain()
	{
		instance = this;
	}

	private DatabaseConnection database;
	private FileConfiguration config = getConfig();
	private static Economy economy;

	private String rtpHead;
	private String rtpCommand;
	private String waterHead;
	private String waterCommand;

	public void genericError(HumanEntity entity)
	{
		entity.sendMessage("Es ist etwas schiefgelaufen versuche es erneut.");
	}

	@SuppressWarnings("unchecked")
	private void loadWorldConfig()
	{
		for (WorldId id : WorldId.values())
		{
			List<Integer> pricesList = (List<Integer>) config.getList(id.toString() + ".prices");

			int[] prices = new int[pricesList.size()];
			for (int i = 0; i < pricesList.size(); i++)
			{
				prices[i] = pricesList.get(i);
			}
			id.setPrices(prices);

			id.setTeleportCommands(config.getList(id.toString() + ".commands").toArray(String[]::new));

			if (id.getPrices().length != id.getTeleportCommands().length)
			{
				getLogger().log(Level.WARNING, String.format("[%s] Length for teleport commands and prices are not the same!", id.toString()));
			}

			id.setPlayerHeadName(config.getString(id.toString() + ".head"));
		}

		rtpHead = config.getString("RTP.head");
		rtpCommand = config.getString("RTP.command");
		
		waterHead = config.getString("WATER.head");
		waterCommand = config.getString("WATER.command");
	}

	@Override
	public void onLoad()
	{
		config.addDefault("database.url", "changeme");
		config.addDefault("database.database", "changeme");
		config.addDefault("database.username", "changeme");
		config.addDefault("database.password", "changeme");
		config.addDefault("database.type", "sqlite");

		config.addDefault("STONE.prices", new int[] {0, 1000, 5000, 15000, 30000, 60000, 100000, 175000, 300000});
		config.addDefault("STONE.commands", new String[] {"say STONE 1", "say STONE 2", "say STONE 3", "say STONE 4", "say STONE 5", "say STONE 6", "say STONE 7", "say STONE 8", "say STONE 9"});
		config.addDefault("STONE.head", "Pexocat");

		config.addDefault("FIRE.prices", new int[] {0, 5000, 25000, 75000, 250000});
		config.addDefault("FIRE.commands", new String[] {"say FIRE 1", "say FIRE 2", "say FIRE 3", "say FIRE 4", "say FIRE 5"});
		config.addDefault("FIRE.head", "Light_2017");

		config.addDefault("WATER.command", "say WATER");
		config.addDefault("WATER.head", "Stoniy");
		
		config.addDefault("RTP.head", "glowman434");
		config.addDefault("RTP.command", "/rtp world world");

		config.options().copyDefaults(true);
		saveConfig();

		try
		{
			getLogger().log(Level.INFO, "Opening database connection");

			String type = config.getString("database.type");
			switch (type)
			{
				case "mysql":
					database = new MySQLDatabaseConnection(config.getString("database.url"), config.getString("database.database"), config.getString("database.username"), config.getString("database.password"));
					break;
				case "sqlite":
					database = new SQLiteDatabaseConnection(new File(getDataFolder(), "levels.db"));
					break;
				default:
					throw new IllegalArgumentException("Unknown database type " + type);
			}
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}

		loadWorldConfig();
	}

	@Override
	public void onEnable()
	{
		if (getServer().getPluginManager().getPlugin("Vault") == null)
		{
			throw new Error("Vault not found!");
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
		{
			throw new Error("Vault economy not found!");
		}
		economy = rsp.getProvider();

		getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);

		getCommand("farm").setExecutor(new FarmworldCommand());
	}

	@Override
	public void onDisable()
	{
		database.close();
	}

	public static FarmWorldMain getInstance()
	{
		return instance;
	}

	public DatabaseConnection getDatabase()
	{
		return database;
	}

	public static Economy getEconomy()
	{
		return economy;
	}

	public String getRtpHead()
	{
		return rtpHead;
	}

	public String getRtpCommand()
	{
		return rtpCommand;
	}
	
	public String getWaterHead()
	{
		return waterHead;
	}
	
	public String getWaterCommand()
	{
		return waterCommand;
	}
}

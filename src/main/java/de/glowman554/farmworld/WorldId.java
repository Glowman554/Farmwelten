package de.glowman554.farmworld;

public enum WorldId
{
	STONE("§7Steinfarmwelt"),
	FIRE("§cFeuer Farmwelt"),
	WATER("§bUnterwasser Stadt")
	;
	
	private String displayName;
	private int[] prices;
	private String[] teleportCommands;
	private String playerHeadName;
	private WorldId(String displayName)
	{
		this.displayName = displayName;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public void setPrices(int[] prices)
	{
		this.prices = prices;
	}
	
	public int[] getPrices()
	{
		return prices;
	}
	
	public String[] getTeleportCommands()
	{
		return teleportCommands;
	}
	
	public void setTeleportCommands(String[] teleportCommands)
	{
		this.teleportCommands = teleportCommands;
	}
	
	public String getPlayerHeadName()
	{
		return playerHeadName;
	}
	
	public void setPlayerHeadName(String playerHeadName)
	{
		this.playerHeadName = playerHeadName;
	}
}

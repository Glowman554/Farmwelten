package de.glowman554.farmworld;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.EconomyResponse;

public class Payments
{
	public static boolean doPayment(Player player, double money)
	{
		if (FarmWorldMain.getEconomy().has(player, money))
		{
			EconomyResponse res = FarmWorldMain.getEconomy().withdrawPlayer(player, money);
			if (res.type != EconomyResponse.ResponseType.SUCCESS)
			{
				return false;
			}
			return true;
		}
		return false;
	}
}

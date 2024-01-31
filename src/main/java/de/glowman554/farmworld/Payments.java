package de.glowman554.farmworld;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class Payments {
    public static boolean doPayment(Player player, double money) {
        if (FarmWorldMain.getInstance().getEconomy().has(player, money)) {
            EconomyResponse res = FarmWorldMain.getInstance().getEconomy().withdrawPlayer(player, money);
            return res.type == EconomyResponse.ResponseType.SUCCESS;
        }
        return false;
    }
}

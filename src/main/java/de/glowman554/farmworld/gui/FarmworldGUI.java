package de.glowman554.farmworld.gui;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.glowman554.farmworld.FarmWorldMain;
import de.glowman554.farmworld.Payments;
import de.glowman554.farmworld.WorldId;

// TODO rename
public class FarmworldGUI implements Listener
{
	private HashMap<HumanEntity, Inventory> inventoryInstances = new HashMap<>();
	
	private int rtpSlot;

	public void openInventoryForPlayer(HumanEntity player) throws SQLException
	{
		Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§a§lLevel §c§lFarmwelten");

		populateInventory(inventory, player);

		inventoryInstances.put(player, inventory);
		player.openInventory(inventory);
	}

	private void populateInventory(Inventory inventory, HumanEntity player) throws SQLException
	{
		int line = 0;
		for (WorldId id : WorldId.values())
		{
			int level = FarmWorldMain.getInstance().getDatabase().readUserWorldLevel(player.getUniqueId(), id);

			int[] prices = id.getPrices();

			for (int i = 0; i < prices.length; i++)
			{
				inventory.setItem(i + line * 9, createLevelItem(id, i + 1, prices[i], level > i));
			}

			line++;
		}
		
		ItemStack rtpStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta rtpMeta = (SkullMeta) rtpStack.getItemMeta();
		rtpMeta.setDisplayName("§aFreie Welt §aFreigeschalten");
		rtpMeta.setOwningPlayer(Bukkit.getOfflinePlayer(FarmWorldMain.getInstance().getRtpHead()));
		rtpStack.setItemMeta(rtpMeta);
		
		rtpSlot = 4 + line * 9;
		inventory.setItem(rtpSlot, rtpStack);
	}

	private ItemStack createLevelItem(WorldId id, int level, int price, boolean unlocked)
	{
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

		itemMeta.setLore(Arrays.asList(String.format("§8Kaufe diese §cFarmwelt für §e%d§8", price)));
		itemMeta.setDisplayName(String.format("%s §aLv%d §a%s", id.getDisplayName(), level, unlocked ? "Freigeschalten" : "Freischaltbar"));
		
		itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(id.getPlayerHeadName()));
		
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	private void doPlayerTeleport(HumanEntity player, WorldId id, int level)
	{
		((Player) player).performCommand(id.getTeleportCommands()[level - 1]);
		player.sendMessage(String.format("§7Erfolgreich nach §eLv%d §aTeleportiert", level));
		player.closeInventory();
	}

	@EventHandler
	public void onPlayerInventoryClick(InventoryClickEvent e)
	{
		if (e.getInventory().equals(inventoryInstances.get(e.getWhoClicked())))
		{
			e.setCancelled(true);

			int rawSlot = e.getRawSlot();
			
			if (rawSlot == rtpSlot)
			{
				((Player) e.getWhoClicked()).performCommand(FarmWorldMain.getInstance().getRtpCommand());
				 e.getWhoClicked().closeInventory();
				return;
			}
			
			WorldId id = WorldId.values()[rawSlot / 9];
			int level = (rawSlot % 9) + 1;
			try
			{
				int dbLevel = FarmWorldMain.getInstance().getDatabase().readUserWorldLevel(e.getWhoClicked().getUniqueId(), id);

				int[] prices = id.getPrices();

				if (prices.length < level)
				{
					return;
				}

				if (level <= dbLevel)
				{
					doPlayerTeleport(e.getWhoClicked(), id, level);
				}
				else
				{
					if (level != dbLevel + 1)
					{
						e.getWhoClicked().sendMessage("§e§lsʟᴏᴡᴍᴄ: §7Kaufe erst die vorherigen §aLevel.");
					}
					else
					{
						if (Payments.doPayment((Player) e.getWhoClicked(), prices[level - 1]))
						{
							FarmWorldMain.getInstance().getDatabase().changeUserWorldLevel(e.getWhoClicked().getUniqueId(), id, level);
							e.getWhoClicked().sendMessage(String.format("§7Erfolgreich §aLevel %d gekauft!", level));
							e.getWhoClicked().closeInventory();
							doPlayerTeleport(e.getWhoClicked(), id, level);
						}
						else
						{
							e.getWhoClicked().sendMessage("§e§lsʟᴏᴡᴍᴄ: §7Du hast zu wenig §eGeld §7für dieses §aLevel.");
						}
					}
				}
			}
			catch (SQLException e1)
			{
				FarmWorldMain.getInstance().genericError(e.getWhoClicked());
			}
		}
	}

	@EventHandler
	public void onPlayerInventoryDragEvent(InventoryDragEvent e)
	{
		if (e.getInventory().equals(inventoryInstances.get(e.getWhoClicked())))
		{
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInventoryClose(InventoryCloseEvent e)
	{
		if (e.getInventory().equals(inventoryInstances.get(e.getPlayer())))
		{
			inventoryInstances.remove(e.getPlayer());
		}
	}
}

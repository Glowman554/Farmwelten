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
import org.bukkit.inventory.meta.SkullMeta;

import de.glowman554.farmworld.FarmWorldMain;
import de.glowman554.farmworld.WorldId;

public class FarmworldMainGUI implements Listener
{
	public FarmworldMainGUI()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, FarmWorldMain.getInstance());

		for (WorldId id : WorldId.values())
		{
			id.setGui(new FarmworldWorldGUI(id));
		}
	}

	private HashMap<HumanEntity, Inventory> inventoryInstances = new HashMap<>();

	private int rtpSlot;
	private int waterSlot;

	public void openInventoryForPlayer(HumanEntity player) throws SQLException
	{
		Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§a§lLevel §c§lFarmwelten");

		populateInventory(inventory, player);

		inventoryInstances.put(player, inventory);
		player.openInventory(inventory);
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack createItem(String displayName, String lore, String head)
	{
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
		itemMeta.setDisplayName(displayName);
		itemMeta.setLore(Arrays.asList(lore.split("\\n")));
		itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(head));
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}

	private void populateInventory(Inventory inventory, HumanEntity player) throws SQLException
	{
		int slot = 10;
		for (WorldId id : WorldId.values())
		{
			inventory.setItem(slot, createItem(id.getDisplayName(), id.getLore(), id.getPlayerHeadName()));
			slot += 2;
		}
		
		waterSlot = slot;
		inventory.setItem(waterSlot, createItem("§b§lᴜɴᴛᴇʀᴡᴀssᴇʀ sᴛᴀᴅᴛ", "§7ᴇʀᴋᴜɴᴅᴇ ᴇɪɴᴇ ɢʀᴏssᴇ §bᴜɴᴛᴇʀᴡᴀssᴇʀsᴛᴀᴅᴛ\n§7ᴍɪᴛ ᴍᴏʙs ʙᴏssᴇɴ §bʟᴏᴏᴛ ᴅᴇs ᴍᴇᴇʀᴇs.", FarmWorldMain.getInstance().getWaterHead()));

		slot += 2;
		
		rtpSlot = slot;
		inventory.setItem(rtpSlot, createItem("§a§lғʀᴇɪᴇ ᴡᴇʟᴛ", "§7ᴇʀᴋᴜɴᴅᴇ ᴇɪɴᴇ ɴᴏʀᴍᴀʟᴇ ᴍɪɴᴇᴄʀᴀғᴛ ᴡᴇʟᴛ ᴀʙᴇʀ\n§cᴀᴄʜᴛᴇ ᴀᴜғ Üʙᴇʀғᴀʟʟᴇ ᴜɴᴅ ᴋᴀᴛᴀsᴛʀᴏᴘʜᴇɴ§7.", FarmWorldMain.getInstance().getRtpHead()));
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
			} else if (rawSlot == waterSlot) {
				((Player) e.getWhoClicked()).performCommand(FarmWorldMain.getInstance().getWaterCommand());
				e.getWhoClicked().closeInventory();
			} else {
				int level = (rawSlot - 10) / 2;
				if (level < WorldId.values().length && level > -1)
				{
					try
					{
						WorldId.values()[level].getGui().openInventoryForPlayer(e.getWhoClicked());
					}
					catch (SQLException e1)
					{
						FarmWorldMain.getInstance().genericError(e.getWhoClicked());
					}
				}
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

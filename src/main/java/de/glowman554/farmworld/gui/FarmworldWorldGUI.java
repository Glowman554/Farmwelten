package de.glowman554.farmworld.gui;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import de.glowman554.farmworld.utils.BungeeUtils;
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
import de.glowman554.farmworld.Payments;
import de.glowman554.farmworld.WorldId;

public class FarmworldWorldGUI implements Listener
{
	private final WorldId worldId;

	public FarmworldWorldGUI(WorldId worldId)
	{
		this.worldId = worldId;
		Bukkit.getServer().getPluginManager().registerEvents(this, FarmWorldMain.getInstance());
	}

	private HashMap<HumanEntity, Inventory> inventoryInstances = new HashMap<>();

	public void openInventoryForPlayer(HumanEntity player) throws SQLException
	{
		Inventory inventory = Bukkit.createInventory(null, 9 * 3, worldId.getDisplayName());

		populateInventory(inventory, player);

		inventoryInstances.put(player, inventory);
		player.openInventory(inventory);
	}

	private void populateInventory(Inventory inventory, HumanEntity player) throws SQLException
	{

		int level = FarmWorldMain.getInstance().getDatabase().readUserWorldLevel(player.getUniqueId(), worldId);

		int[] prices = worldId.getPrices();

		for (int i = 0; i < prices.length; i++)
		{
			inventory.setItem(i, createLevelItem(worldId, i + 1, prices[i], level > i));
		}

	}

	private ItemStack createLevelItem(WorldId id, int level, int price, boolean unlocked)
	{
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

		itemMeta.setLore(Arrays.asList(String.format("§7§lᴋᴏsᴛᴇɴ: §e§l%d$", price)));
		itemMeta.setDisplayName(String.format("§a§lʟᴇᴠᴇʟ: %d %s", level, unlocked ? "§aғʀᴇɪɢᴇsᴄʜᴀʟᴛᴇɴ" : "§cᴋᴀᴜғʙᴀʀ"));

		itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(id.getPlayerHeadName()));

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	private boolean doCompanion(HumanEntity player, WorldId id, int level) {
		if (FarmWorldMain.getInstance().getCompanionSupport()) {
			try {
				FarmWorldMain.getInstance().getDatabase().scheduleUserTeleport(player.getUniqueId(), id, level);
				BungeeUtils.sendPlayer((Player) player, "mine");
			} catch (SQLException e) {
				FarmWorldMain.getInstance().genericError(player);
				throw new RuntimeException(e);
			}
			return true;
		}
		return false;
	}

	private void doPlayerTeleport(HumanEntity player, WorldId id, int level)
	{
		if (!doCompanion(player, id, level)) {
			((Player) player).performCommand(id.getTeleportCommands()[level - 1]);
			player.sendMessage(String.format("§7Erfolgreich nach §eLv%d §aTeleportiert", level));
		}
		player.closeInventory();
	}

	@EventHandler
	public void onPlayerInventoryClick(InventoryClickEvent e)
	{
		if (e.getInventory().equals(inventoryInstances.get(e.getWhoClicked())))
		{
			e.setCancelled(true);

			int rawSlot = e.getRawSlot();

			
			int level = rawSlot + 1;
			try
			{
				int dbLevel = FarmWorldMain.getInstance().getDatabase().readUserWorldLevel(e.getWhoClicked().getUniqueId(), worldId);

				int[] prices = worldId.getPrices();

				if (prices.length < level)
				{
					return;
				}

				if (level <= dbLevel)
				{
					doPlayerTeleport(e.getWhoClicked(), worldId, level);
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
							FarmWorldMain.getInstance().getDatabase().changeUserWorldLevel(e.getWhoClicked().getUniqueId(), worldId, level);
							e.getWhoClicked().sendMessage(String.format("§7Erfolgreich §aLevel %d gekauft!", level));
							e.getWhoClicked().closeInventory();
							doPlayerTeleport(e.getWhoClicked(), worldId, level);
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

package de.glowman554.farmworld.commands;

import de.glowman554.farmworld.FarmWorldMain;
import de.glowman554.farmworld.gui.FarmworldMainGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class FarmworldCommand implements CommandExecutor {
    private final FarmworldMainGUI gui = new FarmworldMainGUI();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if ((sender instanceof Player player)) {
            try {
                gui.openInventoryForPlayer(player);
            } catch (SQLException e) {
                FarmWorldMain.getInstance().genericError(player);
            }
        } else {
            sender.sendMessage("Player only command");
        }
        return false;

    }

}

package de.glowman554.farmworld.papi;

import de.glowman554.farmworld.FarmWorldMain;
import de.glowman554.farmworld.WorldId;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

// steinminenlevel
public class MineLevelExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", FarmWorldMain.getInstance().getDescription().getAuthors()); //
    }

    @Override
    public @NotNull String getIdentifier() {
        return "minenlevel";
    }

    @Override
    public @NotNull String getVersion() {
        return FarmWorldMain.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        try {
            WorldId id = WorldId.valueOf(params);
            return String.valueOf(FarmWorldMain.getInstance().getDatabase().readUserWorldLevel(player.getUniqueId(), id));
        } catch (IllegalArgumentException | SQLException e) {
            return null;
        }
    }
}

package de.glowman554.farmworld;

import de.glowman554.farmworld.gui.FarmworldWorldGUI;

public enum WorldId {
    STONE("§7§lsᴛᴇɪɴғᴀʀᴍ", "§7ғᴀʀᴍᴇ ɪɴ ᴅɪᴇsᴇʀ §aғᴀʀᴍᴡᴇʟᴛ §cᴇʀᴢᴇ §7ᴇʀʜöʜᴇ ᴅᴀs\nʟᴇᴠᴇʟ ᴜɴᴅ ʙᴇᴋᴏᴍᴍᴇ ᴍᴇʜʀ ᴜɴᴅ §eʙᴇssᴇʀᴇ ᴇʀᴢᴇ§7."),
    FIRE("§c§lғᴇᴜᴇʀғᴀʀᴍ", "§7ғᴀʀᴍᴇ ɪɴ ᴅɪᴇsᴇʀ §aғᴀʀᴍᴡᴇʟᴛ §cᴇʀᴢᴇ  ᴅᴇs ɴᴇᴛʜᴇʀs\n§7ᴇʀʜöʜᴇ ᴅᴀs ʟᴇᴠᴇʟ ᴜɴᴅ ʙᴇᴋᴏᴍᴍᴇ ᴍᴇʜʀ ᴜɴᴅ §eʙᴇssᴇʀᴇ ᴇʀᴢᴇ§7."),
    // WATER("§bUnterwasser Stadt")
    ;

    private final String displayName;
    private final String lore;
    private int[] prices;
    private String[] teleportCommands;
    private String playerHeadName;

    private FarmworldWorldGUI gui;

    WorldId(String displayName, String lore) {
        this.displayName = displayName;
        this.lore = lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLore() {
        return lore;
    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public String[] getTeleportCommands() {
        return teleportCommands;
    }

    public void setTeleportCommands(String[] teleportCommands) {
        this.teleportCommands = teleportCommands;
    }

    public String getPlayerHeadName() {
        return playerHeadName;
    }

    public void setPlayerHeadName(String playerHeadName) {
        this.playerHeadName = playerHeadName;
    }

    public FarmworldWorldGUI getGui() {
        return gui;
    }

    public void setGui(FarmworldWorldGUI gui) {
        this.gui = gui;
    }
}

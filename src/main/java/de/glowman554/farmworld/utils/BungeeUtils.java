package de.glowman554.farmworld.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.glowman554.farmworld.FarmWorldMain;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class BungeeUtils implements Listener {
    public static void init() {
        FarmWorldMain.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(FarmWorldMain.getInstance(), "BungeeCord");
        FarmWorldMain.getInstance().getServer().getMessenger().registerIncomingPluginChannel(FarmWorldMain.getInstance(), "BungeeCord", new PluginMessageListener() {
            @Override
            public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {

            }
        });
    }

    public static void sendPlayer(Player player, String server) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("Connect");
        dataOutput.writeUTF(server);
        player.sendPluginMessage(FarmWorldMain.getInstance(), "BungeeCord", dataOutput.toByteArray());
    }
}

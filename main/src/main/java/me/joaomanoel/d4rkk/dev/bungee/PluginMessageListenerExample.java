package me.joaomanoel.d4rkk.dev.bungee;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PluginMessageListenerExample implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String subchannel = in.readUTF();
            if (subchannel.equals("GetServer")) {
                String serverName = in.readUTF();
                player.sendMessage("Você está no servidor: " + serverName);

                updatePlayerServerInfo(player, serverName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePlayerServerInfo(Player player, String serverName) {
        player.sendMessage("O servidor do jogador foi atualizado para: " + serverName);
    }
}

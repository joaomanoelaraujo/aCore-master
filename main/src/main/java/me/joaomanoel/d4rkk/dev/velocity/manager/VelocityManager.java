package me.joaomanoel.d4rkk.dev.velocity.manager;

import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.libraries.profile.Mojang;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityManager {

    public static String getSkin(String player, String type) {
        try {
            String id = Mojang.getUUID(player);
            if (id != null) {
                String textures = Mojang.getSkinProperty(id);
                if (textures != null) {
                    return type.equalsIgnoreCase("value")
                            ? textures.split(" : ")[1]
                            : textures.split(" : ")[2];
                }
            }
        } catch (Exception ignore) {
        }

        return VelocityPlugin.STEVE;
    }

    public static void sendMessage(Player player, String message) {
        Component component = LegacyComponentSerializer.legacySection()
                .deserialize(message);
        player.sendMessage(component);
    }

    public static String getName(Player player) {
        return player.getUsername();
    }

    public static Player getPlayer(String name) {
        return VelocityPlugin.getInstance()
                .getServer()
                .getPlayer(name)
                .orElse(null);
    }

    public static String getCurrent(String playerName) {
        return VelocityPlugin.getCurrent(playerName);
    }

    public static String getFake(String playerName) {
        return VelocityPlugin.getFake(playerName);
    }

    public static Role getFakeRole(String playerName) {
        return VelocityPlugin.getRole(playerName);
    }

    public static boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    public static boolean isFake(String playerName) {
        return VelocityPlugin.isFake(playerName);
    }
}
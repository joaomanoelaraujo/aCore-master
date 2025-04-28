package me.joaomanoel.d4rkk.dev.nms;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.utility.MinecraftVersion;
import me.joaomanoel.d4rkk.dev.nms.hologram.HologramEntity;
import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;

public class NMSManager {

    public static NMS_Interface instance;

    public static void setupNMS(JavaPlugin plugin) {
        MinecraftVersion version = getProtocolManager().getMinecraftVersion();
        String[] finalClassName = getClass(version);
        try {
            Class<?> clazz = Class.forName(finalClassName[0]);
            Constructor<?> constructor = clazz.getConstructor();
            instance = (NMS_Interface) constructor.newInstance();

            clazz = Class.forName(finalClassName[1]);
            constructor = clazz.getConstructor();
            BukkitUtils.setInstance((BukkitUtilsItf) constructor.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        instance.setupListeners(plugin);
    }

    private static String[] getClass(MinecraftVersion version) {
        String[] finalClassName = new String[2];
        switch (version.getVersion()) {
            case "1.20.6": {
                finalClassName[0] = "me.joaomanoel.d4rkk.dev.nms.NMS1_20_R2";
                finalClassName[1] = "me.joaomanoel.d4rkk.dev.nms.BukkitUtils_1_20_R2";
                break;
            }

            case "1.8.8": {
                finalClassName[0] = "me.joaomanoel.d4rkk.dev.nms.NMS1_8_R3";
                finalClassName[1] = "me.joaomanoel.d4rkk.dev.nms.BukkitUtils_1_8_R3";
                break;
            }

            default: {
                finalClassName = null;
                break;
            }
        }

        if (finalClassName == null) {
            throw new RuntimeException("Could not find NMS class for version " + version);
        }
        return finalClassName;
    }

    public static void setupListeners(JavaPlugin plugin) {
        instance.setupListeners(plugin);
    }

    public static void sendActionBar(String text, Player player) {
        instance.sendActionBar(text, player);
    }

    public static void createMountableEnderDragon(Player player) {
        instance.createMountableEnderDragon(player);
    }

    public static void sendTablist(String header, String footer, Player player) {
        instance.sendTablist(header, footer, player);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        instance.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        instance.sendTitle(player, title, subtitle);
    }

    public static void setValueAndSignature(Player player, String value, String signature) {
        instance.setValueAndSignature(player, value, signature);
    }

    public static void playChestAction(Location location, boolean open) {
        instance.playChestAction(location, open);
    }

    public static void sendTabListAdd(Player player, Player listPlayer) {
        instance.sendTabListAdd(player, listPlayer);
    }

    public static void sendTabListRemove(Player player, Player listPlayer) {
        instance.sendTabListRemove(player, listPlayer);
    }

    public static void clearPathfinderGoal(Object entity) {
        instance.clearPathfinderGoal(entity);
    }

    public static void addToWorld(World world, Object entity, CreatureSpawnEvent.SpawnReason reason) {
        instance.addToWorld(world, entity, reason);
    }

    public static void removeToWorld(Object entity) {
        instance.removeToWorld(entity);
    }

    public static void look(Object entity, float yaw, float pitch) {
        instance.look(entity, yaw, pitch);
    }

    public static void setHeadYaw(Object entity, float yaw) {
        instance.setHeadYaw(entity, yaw);
    }

    public static HologramEntity createHologram(Location location) {
        return instance.createHologram(location);
    }

    public static NpcEntity createNPC(Location location, String name, String value, String signature) {
        return instance.createNPC(location, name, value, signature);
    }

    public static void refreshPlayer(Player player) {
        instance.refreshPlayer(player);
    }

    public static float clampYaw(float yaw) {
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }
        while (yaw >= 180.0F) {
            yaw -= 360.0F;
        }

        return yaw;
    }

    public static ProtocolManager getProtocolManager() {
        return ProtocolLibrary.getProtocolManager();
    }
}

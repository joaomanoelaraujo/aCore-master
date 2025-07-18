package me.joaomanoel.d4rkk.dev.nms;

import me.joaomanoel.d4rkk.dev.nms.hologram.HologramEntity;
import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface NMS_Interface {


    void setupListeners(JavaPlugin plugin);
    void createMountableEnderDragon(Player player);
    void sendActionBar(String text, Player player);
    void sendTablist(String header, String footer, Player player);
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    void sendTitle(Player player, String title, String subtitle);
    void setValueAndSignature(Player player, String value, String signature);
    void playChestAction(Location location, boolean open);
    void sendTabListAdd(Player player, Player listPlayer);
    void sendTabListRemove(Player player, Player listPlayer);
    void clearPathfinderGoal(Object entity);
    void addToWorld(World world, Object entity, CreatureSpawnEvent.SpawnReason reason);
    void removeToWorld(Object entity);
    void look(Object entity, float yaw, float pitch);
    void setHeadYaw(Object entity, float yaw);
    void refreshPlayer(Player player);
    HologramEntity createHologram(Location location);
    NpcEntity createNPC(Location location, String name, String value, String signature);

}

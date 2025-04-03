package me.joaomanoel.d4rkk.dev.nms;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.libraries.MinecraftVersion;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.Hologram;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.HologramLine;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPCAnimation;
import me.joaomanoel.d4rkk.dev.libraries.npclib.npc.skin.SkinnableEntity;
import me.joaomanoel.d4rkk.dev.nms.interfaces.INMS;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IArmorStand;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IItem;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.ISlime;
import me.joaomanoel.d4rkk.dev.nms.v1_8_R3.NMS1_8R3;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class NMS {
  
  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("NMS");
  private static INMS BRIDGE;
  
  public static IArmorStand createArmorStand(Location location, String name, HologramLine line) {
    return BRIDGE.createArmorStand(location, name, line);
  }
  
  public static IItem createItem(Location location, ItemStack item, HologramLine line) {
    return BRIDGE.createItem(location, item, line);
  }
  
  public static void clearPathfinderGoal(Object entity) {
    BRIDGE.clearPathfinderGoal(entity);
  }
  
  public static void playChestAction(Location location, boolean open) {
    BRIDGE.playChestAction(location, open);
  }
  
  public static ISlime createSlime(Location location, HologramLine line) {
    return BRIDGE.createSlime(location, line);
  }
  
  public static Hologram getHologram(Entity entity) {
    return BRIDGE.getHologram(entity);
  }
  
  public static Hologram getPreHologram(int entityId) {
    return BRIDGE.getPreHologram(entityId);
  }
  
  public static boolean isHologramEntity(Entity entity) {
    return BRIDGE.isHologramEntity(entity);
  }
  
  public static void playAnimation(Entity entity, NPCAnimation animation) {
    BRIDGE.playAnimation(entity, animation);
  }
  
  public static void setValueAndSignature(Player player, String value, String signature) {
    BRIDGE.setValueAndSignature(player, value, signature);
  }
  
  public static void sendTabListAdd(Player player, Player listPlayer) {
    BRIDGE.sendTabListAdd(player, listPlayer);
  }
  
  public static void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities) {
    BRIDGE.sendTabListRemove(player, skinnableEntities);
  }
  
  public static void sendTabListRemove(Player player, Player listPlayer) {
    BRIDGE.sendTabListRemove(player, listPlayer);
  }
  
  public static void removeFromPlayerList(Player player) {
    BRIDGE.removeFromPlayerList(player);
  }
  
  public static void removeFromServerPlayerList(Player player) {
    BRIDGE.removeFromServerPlayerList(player);
  }
  
  public static boolean addToWorld(World world, Entity entity, SpawnReason reason) {
    return BRIDGE.addToWorld(world, entity, reason);
  }
  
  public static void removeFromWorld(Entity entity) {
    BRIDGE.removeFromWorld(entity);
  }
  
  public static void replaceTrackerEntry(Player player) {
    BRIDGE.replaceTrackerEntry(player);
  }
  
  public static void sendPacket(Player player, Object packet) {
    BRIDGE.sendPacket(player, packet);
  }
  
  public static void look(Entity entity, float yaw, float pitch) {
    BRIDGE.look(entity, yaw, pitch);
  }
  
  public static void setHeadYaw(Entity entity, float yaw) {
    BRIDGE.setHeadYaw(entity, yaw);
  }
  
  public static void setStepHeight(LivingEntity entity, float height) {
    BRIDGE.setStepHeight(entity, height);
  }
  
  public static float getStepHeight(LivingEntity entity) {
    return BRIDGE.getStepHeight(entity);
  }
  
  public static SkinnableEntity getSkinnable(Entity entity) {
    return BRIDGE.getSkinnable(entity);
  }
  
  public static void flyingMoveLogic(LivingEntity entity, float f, float f1) {
    BRIDGE.flyingMoveLogic(entity, f, f1);
  }
  
  public static void sendActionBar(Player player, String message) {
    BRIDGE.sendActionBar(player, message);
  }
  
  public static void sendTitle(Player player, String title, String subtitle) {
    BRIDGE.sendTitle(player, title, subtitle);
  }
  
  public static void refreshPlayer(Player player) {
    BRIDGE.refreshPlayer(player);
  }
  
  public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    BRIDGE.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
  }
  
  public static void sendTabHeaderFooter(Player player, String header, String footer) {
    BRIDGE.sendTabHeaderFooter(player, header, footer);
  }
  
    public static boolean setupNMS() {
      if (MinecraftVersion.getCurrentVersion().getCompareId() == 183) {
        BRIDGE = new NMS1_8R3();
        return true;
      }

      return false;
    }
  }
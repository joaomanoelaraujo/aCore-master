package me.joaomanoel.d4rkk.dev.titles;

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TitleController {
  private final Player owner;
  private String name;
  private int entityId;
  private boolean enabled;

  public TitleController(Player owner, String name) {
    this.owner = owner;
    this.name = name;
    this.entityId = generateEntityId();
  }

  public void enable() {
    if (this.enabled) return;
    this.enabled = true;

    for (Player player : owner.getWorld().getPlayers()) {
      if (player.canSee(owner)) {
        showToPlayer(player);
      }
    }
  }

  public void disable() {
    if (!this.enabled) return;
    this.enabled = false;

    for (Player player : owner.getWorld().getPlayers()) {
      hideToPlayer(player);
    }
  }

  public void showToPlayer(Player player) {
    if (!this.enabled || player == null) return;

    try {
      Location loc = owner.getLocation().add(0, 2.25, 0);

      WrapperPlayServerSpawnEntityLiving packet = new WrapperPlayServerSpawnEntityLiving();
      packet.setEntityID(entityId);
      packet.setType(EntityType.ARMOR_STAND);

      // Set position using protocol lib methods
      packet.setX(loc.getX());
      packet.setY(loc.getY());
      packet.setZ(loc.getZ());
      packet.setYaw(0);
      packet.setPitch(0);

      // Set metadata
      WrappedDataWatcher watcher = new WrappedDataWatcher();

      // 0x20 = Invisible flag
      watcher.setObject(0, (byte) 0x20);

      // Custom name
      watcher.setObject(2, this.name);

      // Custom name visible
      watcher.setObject(3, (byte) 1);

      // No gravity
      if (watcher.getWatchableObjects().size() > 10) {
        watcher.setObject(10, (byte) 1);
      }

      // Small size
      if (watcher.getWatchableObjects().size() > 11) {
        watcher.setObject(11, (byte) 1);
      }

      // Marker (no hitbox)
      if (watcher.getWatchableObjects().size() > 14) {
        watcher.setObject(14, (byte) 1);
      }

      packet.setMetadata(watcher);
      packet.sendPacket(player);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void hideToPlayer(Player player) {
    if (player == null) return;

    try {
      WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
      packet.setEntityIds(new int[]{entityId});
      packet.sendPacket(player);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void destroy() {
    disable();
  }

  public void setName(String name) {
    this.name = name;
    if (this.enabled) {
      disable();
      enable();
    }
  }

  public Player getOwner() {
    return owner;
  }

  private int generateEntityId() {
    return (int) (Math.random() * Integer.MAX_VALUE);
  }
}
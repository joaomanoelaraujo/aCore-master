package me.joaomanoel.d4rkk.dev.libraries.holograms.api;

import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IArmorStand;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IItem;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.ISlime;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class HologramLine {
  
  private final Location location;
  private IArmorStand armor;
  private ISlime slime;
  private IItem item;
  private TouchHandler touch;
  private PickupHandler pickup;
  private String line;
  private final Hologram hologram;
  
  public HologramLine(Hologram hologram, Location location, String line) {
    this.line = StringUtils.formatColors(line);
    this.location = location;
    this.armor = null;
    this.hologram = hologram;
  }
  
  public void spawn() {
    if (this.armor == null) {
      this.armor = NMS.createArmorStand(location, line, this);
      
      if (this.touch != null) {
        this.setTouchable(this.touch);
      }
    }
  }
  
  public void despawn() {
    if (this.armor != null) {
      this.armor.killEntity();
      this.armor = null;
    }
    if (this.slime != null) {
      this.slime.killEntity();
      this.slime = null;
    }
    if (this.item != null) {
      this.item.killEntity();
      this.item = null;
    }
  }
  
  public void setTouchable(TouchHandler touch) {
    if (touch == null) {
      this.slime.killEntity();
      this.slime = null;
      this.touch = null;
      return;
    }
    
    if (armor != null) {
      this.slime = slime == null ? NMS.createSlime(location, this) : slime;
      
      if (this.slime != null) {
        this.slime.setPassengerOf(this.armor.getEntity());
      }
      
      this.touch = touch;
    }
  }
  
  public void setItem(ItemStack item, PickupHandler pickup) {
    if (pickup == null) {
      this.item.killEntity();
      this.item = null;
      this.pickup = null;
      return;
    }
    
    if (armor != null) {
      this.item = this.item == null ? NMS.createItem(location, item, this) : this.item;
      
      if (this.item != null) {
        this.item.setPassengerOf(this.armor.getEntity());
      }
      
      this.pickup = pickup;
    }
  }
  
  public Location getLocation() {
    return this.location;
  }
  
  public void setLocation(Location location) {
    if (this.armor != null) {
      this.armor.setLocation(location.getX(), location.getY(), location.getZ());
      if (this.slime != null) {
        this.slime.setPassengerOf(this.armor.getEntity());
      }
    }
  }
  
  public IArmorStand getArmor() {
    return this.armor;
  }
  
  public ISlime getSlime() {
    return this.slime;
  }
  
  public TouchHandler getTouchHandler() {
    return this.touch;
  }
  
  public PickupHandler getPickupHandler() {
    return this.pickup;
  }
  
  public String getLine() {
    return this.line;
  }
  
  public void setLine(String line) {
    if (this.line.equals(StringUtils.formatColors(line))) {
      this.armor.setName(this.line + "§r");
      this.line = this.line + "§r";
      return;
    }
    
    this.line = StringUtils.formatColors(line);
    if (armor == null) {
      if (hologram.isSpawned()) {
        this.spawn();
      }
      
      return;
    }
    
    this.armor.setName(this.line);
  }
  
  public Hologram getHologram() {
    return this.hologram;
  }
}

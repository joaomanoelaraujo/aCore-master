package me.joaomanoel.d4rkk.dev.nms.interfaces.entity;

import me.joaomanoel.d4rkk.dev.libraries.holograms.api.HologramLine;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public interface IItem {
  
  void setPassengerOf(Entity entity);
  
  void setItemStack(ItemStack item);
  
  void setLocation(double x, double y, double z);
  
  boolean isDead();
  
  void killEntity();
  
  Item getEntity();
  
  HologramLine getLine();
}

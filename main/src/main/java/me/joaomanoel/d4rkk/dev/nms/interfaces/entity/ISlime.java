package me.joaomanoel.d4rkk.dev.nms.interfaces.entity;

import me.joaomanoel.d4rkk.dev.libraries.holograms.api.HologramLine;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;

public interface ISlime {
  
  void setPassengerOf(Entity entity);
  
  void setLocation(double x, double y, double z);
  
  boolean isDead();
  
  void killEntity();
  
  Slime getEntity();
  
  HologramLine getLine();
}

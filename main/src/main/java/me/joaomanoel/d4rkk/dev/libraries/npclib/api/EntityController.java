package me.joaomanoel.d4rkk.dev.libraries.npclib.api;

import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface EntityController {
  
  void spawn(Location location, NPC npc);
  
  void remove();
  
  Entity getBukkitEntity();
}

package me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc;

import me.joaomanoel.d4rkk.dev.libraries.npclib.api.metadata.MetadataStore;
import me.joaomanoel.d4rkk.dev.libraries.npclib.trait.NPCTrait;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public interface NPC {
  
  // boolean
  String PROTECTED_KEY = "protected",
  // boolean
  TAB_LIST_KEY = "hide-from-tablist",
  // boolean
  HIDE_BY_TEAMS_KEY = "hide-by-teams",
  // boolean
  FLYABLE = "flyable",
  // boolean
  GRAVITY = "gravity",
  // nome do player
  ATTACHED_PLAYER = "only-for",
  // boolean
  COPY_PLAYER_SKIN = "copy-player";
  
  boolean spawn(Location location);
  
  boolean despawn();
  
  void destroy();
  
  void update();
  
  MetadataStore data();

  void playAnimation(NPCAnimation animation);
  
  void addTrait(NPCTrait trait);
  
  void addTrait(Class<? extends NPCTrait> traitClass);
  
  void removeTrait(Class<? extends NPCTrait> traitClass);
  
  void finishNavigation();
  
  boolean isSpawned();
  
  boolean isProtected();
  
  boolean isLaying();
  
  void setLaying(boolean laying);
  
  boolean isNavigating();
  
  <T extends NPCTrait> T getTrait(Class<T> traitClass);
  
  Entity getEntity();
  
  Entity getFollowing();
  
  void setFollowing(Entity entity);
  
  Location getWalkingTo();
  
  void setWalkingTo(Location location);
  
  Location getCurrentLocation();
  
  UUID getUUID();
  
  String getName();
}

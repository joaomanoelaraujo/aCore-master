package me.joaomanoel.d4rkk.dev.libraries.npclib.trait;

import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.trait.Trait;

public abstract class NPCTrait implements Trait {
  
  private final NPC npc;
  
  public NPCTrait(NPC npc) {
    this.npc = npc;
  }
  
  public NPC getNPC() {
    return npc;
  }
  
  @Override
  public void onAttach() {
  }
  
  @Override
  public void onSpawn() {
  }
  
  @Override
  public void onDespawn() {
  }
  
  @Override
  public void onRemove() {
  }
}

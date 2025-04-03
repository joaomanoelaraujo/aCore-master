package me.joaomanoel.d4rkk.dev.libraries.npclib.api.trait;

public interface Trait {
  
  /**
   * chamado ao Rastreio ser adicionado.
   */
  void onAttach();
  
  /**
   * chamado ao Rastreio ser removido.
   */
  void onRemove();
  
  /**
   * chamado ao NPC ser spawnado.
   */
  void onSpawn();
  
  /**
   * chamado ao NPC ser despawnado.
   */
  void onDespawn();
}

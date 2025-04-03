package me.joaomanoel.d4rkk.dev.libraries.npclib.api.event;

import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NPCDeathEvent extends NPCEvent implements Cancellable {
  
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final NPC npc;
  private final Player killer;
  private boolean cancelled;
  
  public NPCDeathEvent(NPC npc, Player killer) {
    this.npc = npc;
    this.killer = killer;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
  
  public NPC getNPC() {
    return npc;
  }
  
  public Player getKiller() {
    return killer;
  }
  
  public boolean isCancelled() {
    return cancelled;
  }
  
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
  
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}

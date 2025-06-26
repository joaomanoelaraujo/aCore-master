package me.joaomanoel.d4rkk.dev.nms.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractAtNpcEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final NpcEntity npc;
    private boolean cancelled;

    public PlayerInteractAtNpcEvent(Player player, NpcEntity npc) {
        this.player = player;
        this.npc    = npc;
    }

    public Player getPlayer() { return player; }
    public NpcEntity getNpc() { return npc; }

    @Override public boolean isCancelled()        { return cancelled; }
    @Override public void setCancelled(boolean c) { this.cancelled = c; }
    @Override public HandlerList getHandlers()    { return HANDLERS; }
    public static HandlerList getHandlerList()    { return HANDLERS; }
}

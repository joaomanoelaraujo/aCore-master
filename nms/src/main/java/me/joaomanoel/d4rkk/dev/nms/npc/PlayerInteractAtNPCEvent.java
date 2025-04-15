package me.joaomanoel.d4rkk.dev.nms.npc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractAtNPCEvent extends Event {

    public static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final NpcEntity npc;

    public PlayerInteractAtNPCEvent(Player player, NpcEntity npc) {
        this.player = player;
        this.npc = npc;
    }

    public Player getPlayer() {
        return player;
    }

    public NpcEntity getNpc() {
        return npc;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

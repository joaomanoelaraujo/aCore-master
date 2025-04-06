/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReplaySessionFinishEvent
extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Replay replay;
    private final Player player;

    public ReplaySessionFinishEvent(Replay replay, Player player) {
        super(!Bukkit.isPrimaryThread());
        this.replay = replay;
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Replay getReplay() {
        return this.replay;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}


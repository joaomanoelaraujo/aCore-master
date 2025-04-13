package me.joaomanoel.d4rkk.dev.libraries.hologram.listeners;

import me.joaomanoel.d4rkk.dev.libraries.hologram.Hologram;
import me.joaomanoel.d4rkk.dev.libraries.hologram.HologramLine;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HologramTouchEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Hologram hologram;
    private final HologramLine line;
    private final Player player;

    public HologramTouchEvent(Hologram hologram, HologramLine line, Player player) {
        this.hologram = hologram;
        this.line = line;
        this.player = player;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public HologramLine getLine() {
        return line;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

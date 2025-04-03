/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class BlockChangeData
extends PacketData {
    private static final long serialVersionUID = -5904448938888041161L;
    private LocationData location;
    private ItemData before;
    private ItemData after;

    public BlockChangeData(LocationData location, ItemData before, ItemData after) {
        this.location = location;
        this.before = before;
        this.after = after;
    }

    public LocationData getLocation() {
        return this.location;
    }

    public ItemData getAfter() {
        return this.after;
    }

    public ItemData getBefore() {
        return this.before;
    }
}


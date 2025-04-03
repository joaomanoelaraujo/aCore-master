/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class WorldChangeData
extends PacketData {
    private static final long serialVersionUID = -7360847147915116994L;
    private LocationData location;

    public WorldChangeData(LocationData location) {
        this.location = location;
    }

    public LocationData getLocation() {
        return this.location;
    }
}


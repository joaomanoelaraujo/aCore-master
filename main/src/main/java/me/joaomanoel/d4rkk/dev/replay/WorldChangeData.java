/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.replay.LocationData;
import me.joaomanoel.d4rkk.dev.replay.PacketData;

public class WorldChangeData
extends PacketData {
    private static final long serialVersionUID = -7360847147915116994L;
    private final LocationData location;

    public WorldChangeData(LocationData location) {
        this.location = location;
    }

    public LocationData getLocation() {
        return this.location;
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class BedEnterData
extends PacketData {
    private static final long serialVersionUID = 6849586468365004854L;
    private LocationData location;

    public BedEnterData(LocationData location) {
        this.location = location;
    }

    public LocationData getLocation() {
        return this.location;
    }
}


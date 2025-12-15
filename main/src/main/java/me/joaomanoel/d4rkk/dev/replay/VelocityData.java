/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.replay.PacketData;

public class VelocityData
extends PacketData {
    private static final long serialVersionUID = 4046621273672598227L;
    private final double x;
    private final double y;
    private final double z;
    private final int id;

    public VelocityData(int id, double x, double y, double z) {
        this.id = id;
        this.y = y;
        this.x = x;
        this.z = z;
    }

    public int getId() {
        return this.id;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}


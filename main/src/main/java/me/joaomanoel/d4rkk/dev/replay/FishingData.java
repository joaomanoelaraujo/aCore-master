/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class FishingData
extends PacketData {
    private static final long serialVersionUID = -3909142114596921006L;
    private LocationData location;
    private double x;
    private double y;
    private double z;
    private int id;

    public FishingData(int id, LocationData location, double x, double y, double z) {
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public LocationData getLocation() {
        return this.location;
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

    public int getId() {
        return this.id;
    }
}


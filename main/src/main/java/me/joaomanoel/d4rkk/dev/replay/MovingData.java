/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class MovingData
extends PacketData {
    private static final long serialVersionUID = -3792160902735306458L;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public MovingData(double x, double y, double z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public double getY() {
        return this.y;
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getX() {
        return this.x;
    }

    public float getYaw() {
        return this.yaw;
    }

    public double getZ() {
        return this.z;
    }
}


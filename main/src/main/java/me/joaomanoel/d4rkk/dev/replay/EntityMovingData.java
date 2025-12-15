/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class EntityMovingData
extends PacketData {
    private static final long serialVersionUID = -3792160902735306458L;
    private final double x;
    private final double y;
    private final double z;
    private final int id;
    private final float pitch;
    private final float yaw;

    public EntityMovingData(int id, double x, double y, double z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
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

    public int getId() {
        return this.id;
    }
}


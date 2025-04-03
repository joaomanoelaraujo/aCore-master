/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.PacketContainer
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientVehicleMove
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.VEHICLE_MOVE;

    public WrapperPlayClientVehicleMove() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientVehicleMove(PacketContainer packet) {
        super(packet, TYPE);
    }

    public double getX() {
        return this.handle.getDoubles().read(0);
    }

    public void setX(double value) {
        this.handle.getDoubles().write(0, value);
    }

    public double getY() {
        return this.handle.getDoubles().read(1);
    }

    public void setY(double value) {
        this.handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return this.handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        this.handle.getDoubles().write(2, value);
    }

    public float getYaw() {
        return this.handle.getFloat().read(0).floatValue();
    }

    public void setYaw(float value) {
        this.handle.getFloat().write(0, Float.valueOf(value));
    }

    public float getPitch() {
        return this.handle.getFloat().read(1).floatValue();
    }

    public void setPitch(float value) {
        this.handle.getFloat().write(1, Float.valueOf(value));
    }
}


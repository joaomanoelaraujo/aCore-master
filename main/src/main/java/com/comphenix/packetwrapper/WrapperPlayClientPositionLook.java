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

public class WrapperPlayClientPositionLook
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.POSITION_LOOK;

    public WrapperPlayClientPositionLook() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientPositionLook(PacketContainer packet) {
        super(packet, TYPE);
    }

    public double getX() {
        return (Double)this.handle.getDoubles().read(0);
    }

    public void setX(double value) {
        this.handle.getDoubles().write(0, value);
    }

    public double getY() {
        return (Double)this.handle.getDoubles().read(1);
    }

    public void setY(double value) {
        this.handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return (Double)this.handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        this.handle.getDoubles().write(2, value);
    }

    public float getYaw() {
        return ((Float)this.handle.getFloat().read(0)).floatValue();
    }

    public void setYaw(float value) {
        this.handle.getFloat().write(0, Float.valueOf(value));
    }

    public float getPitch() {
        return ((Float)this.handle.getFloat().read(1)).floatValue();
    }

    public void setPitch(float value) {
        this.handle.getFloat().write(1, Float.valueOf(value));
    }

    public boolean getOnGround() {
        return (Boolean)this.handle.getBooleans().read(0);
    }

    public void setOnGround(boolean value) {
        this.handle.getBooleans().write(0, value);
    }
}


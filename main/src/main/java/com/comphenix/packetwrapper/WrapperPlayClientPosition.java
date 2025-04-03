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

public class WrapperPlayClientPosition
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.POSITION;

    public WrapperPlayClientPosition() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientPosition(PacketContainer packet) {
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

    public boolean getOnGround() {
        return (Boolean)this.handle.getBooleans().read(0);
    }

    public void setOnGround(boolean value) {
        this.handle.getBooleans().write(0, value);
    }
}


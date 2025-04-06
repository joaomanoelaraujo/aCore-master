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

public class WrapperPlayClientLook
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.LOOK;

    public WrapperPlayClientLook() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientLook(PacketContainer packet) {
        super(packet, TYPE);
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


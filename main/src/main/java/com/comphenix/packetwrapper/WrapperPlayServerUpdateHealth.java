/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketContainer
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerUpdateHealth
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.UPDATE_HEALTH;

    public WrapperPlayServerUpdateHealth() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerUpdateHealth(PacketContainer packet) {
        super(packet, TYPE);
    }

    public float getHealth() {
        return ((Float)this.handle.getFloat().read(0)).floatValue();
    }

    public void setHealth(float value) {
        this.handle.getFloat().write(0, Float.valueOf(value));
    }

    public int getFood() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public void setFood(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public float getFoodSaturation() {
        return ((Float)this.handle.getFloat().read(1)).floatValue();
    }

    public void setFoodSaturation(float value) {
        this.handle.getFloat().write(1, Float.valueOf(value));
    }
}


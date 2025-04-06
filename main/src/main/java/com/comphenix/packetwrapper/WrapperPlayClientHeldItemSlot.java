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

public class WrapperPlayClientHeldItemSlot
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.HELD_ITEM_SLOT;

    public WrapperPlayClientHeldItemSlot() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientHeldItemSlot(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getSlot() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public void setSlot(int value) {
        this.handle.getIntegers().write(0, value);
    }
}


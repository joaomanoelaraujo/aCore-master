/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.wrappers.EnumWrappers$Hand
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class WrapperPlayClientBlockPlace
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.BLOCK_PLACE;

    public WrapperPlayClientBlockPlace() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientBlockPlace(PacketContainer packet) {
        super(packet, TYPE);
    }

    public EnumWrappers.Hand getHand() {
        return (EnumWrappers.Hand)this.handle.getHands().read(0);
    }

    public void setHand(EnumWrappers.Hand value) {
        this.handle.getHands().write(0, value);
    }

    public long getTimestamp() {
        return (Long)this.handle.getLongs().read(0);
    }

    public void setTimestamp(long value) {
        this.handle.getLongs().write(0, value);
    }
}


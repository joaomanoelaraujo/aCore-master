/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.wrappers.BlockPosition
 *  com.comphenix.protocol.wrappers.EnumWrappers$Direction
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerDigType
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class WrapperPlayClientBlockDig
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.BLOCK_DIG;

    public WrapperPlayClientBlockDig() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientBlockDig(PacketContainer packet) {
        super(packet, TYPE);
    }

    public BlockPosition getLocation() {
        return (BlockPosition)this.handle.getBlockPositionModifier().read(0);
    }

    public void setLocation(BlockPosition value) {
        this.handle.getBlockPositionModifier().write(0, value);
    }

    public EnumWrappers.Direction getDirection() {
        return (EnumWrappers.Direction)this.handle.getDirections().read(0);
    }

    public void setDirection(EnumWrappers.Direction value) {
        this.handle.getDirections().write(0, value);
    }

    public EnumWrappers.PlayerDigType getStatus() {
        return (EnumWrappers.PlayerDigType)this.handle.getPlayerDigTypes().read(0);
    }

    public void setStatus(EnumWrappers.PlayerDigType value) {
        this.handle.getPlayerDigTypes().write(0, value);
    }
}


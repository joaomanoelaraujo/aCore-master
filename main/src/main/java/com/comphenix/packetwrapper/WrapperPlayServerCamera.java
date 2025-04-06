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

public class WrapperPlayServerCamera
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.CAMERA;

    public WrapperPlayServerCamera() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerCamera(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getCameraId() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public void setCameraId(int value) {
        this.handle.getIntegers().write(0, value);
    }
}


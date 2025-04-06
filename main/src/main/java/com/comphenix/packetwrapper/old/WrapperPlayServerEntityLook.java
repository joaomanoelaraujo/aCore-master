/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 */
package com.comphenix.packetwrapper.old;

import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntityLook
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_LOOK;

    public WrapperPlayServerEntityLook() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityLook(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityID() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public Entity getEntity(World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }

    public float getYaw() {
        return (float)((Byte)this.handle.getBytes().read(0)).byteValue() * 360.0f / 256.0f;
    }

    public void setYaw(float value) {
        this.handle.getBytes().write(0, ((byte)(value * 256.0f / 360.0f)));
    }

    public float getPitch() {
        return (float)((Byte)this.handle.getBytes().read(1)).byteValue() * 360.0f / 256.0f;
    }

    public void setPitch(float value) {
        this.handle.getBytes().write(1, ((byte)(value * 256.0f / 360.0f)));
    }

    public boolean getOnGround() {
        return (Boolean)this.handle.getSpecificModifier(Boolean.TYPE).read(0);
    }

    public void setOnGround(boolean value) {
        this.handle.getSpecificModifier(Boolean.TYPE).write(0, value);
    }
}


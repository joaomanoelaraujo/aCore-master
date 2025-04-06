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
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntityEffect
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_EFFECT;

    public WrapperPlayServerEntityEffect() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityEffect(PacketContainer packet) {
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

    public byte getEffectID() {
        return (Byte)this.handle.getBytes().read(0);
    }

    public void setEffectID(byte value) {
        this.handle.getBytes().write(0, ((byte)(value & 0xFF)));
    }

    public byte getAmplifier() {
        return (Byte)this.handle.getBytes().read(1);
    }

    public void setAmplifier(byte value) {
        this.handle.getBytes().write(1, ((byte)(value & 0xFF)));
    }

    public int getDuration() {
        return (Integer)this.handle.getIntegers().read(1);
    }

    public void setDuration(int value) {
        this.handle.getIntegers().write(1, value);
    }

    public boolean getHideParticles() {
        return (Byte)this.handle.getBytes().read(2) == 0;
    }

    public void setHideParticles(boolean value) {
        this.handle.getBytes().write(2, ((byte)(!value ? 1 : 0)));
    }
}


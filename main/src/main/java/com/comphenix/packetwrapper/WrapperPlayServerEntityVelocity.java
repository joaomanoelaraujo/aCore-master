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

public class WrapperPlayServerEntityVelocity
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_VELOCITY;

    public WrapperPlayServerEntityVelocity() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityVelocity(PacketContainer packet) {
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

    public double getVelocityX() {
        return (double)((Integer)this.handle.getIntegers().read(1)).intValue() / 8000.0;
    }

    public void setVelocityX(double value) {
        this.handle.getIntegers().write(1, ((int)(value * 8000.0)));
    }

    public double getVelocityY() {
        return (double)((Integer)this.handle.getIntegers().read(2)).intValue() / 8000.0;
    }

    public void setVelocityY(double value) {
        this.handle.getIntegers().write(2, ((int)(value * 8000.0)));
    }

    public double getVelocityZ() {
        return (double)((Integer)this.handle.getIntegers().read(3)).intValue() / 8000.0;
    }

    public void setVelocityZ(double value) {
        this.handle.getIntegers().write(3, ((int)(value * 8000.0)));
    }
}


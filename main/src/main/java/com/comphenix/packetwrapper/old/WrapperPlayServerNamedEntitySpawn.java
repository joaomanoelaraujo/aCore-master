/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.wrappers.WrappedDataWatcher
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.util.Vector
 */
package com.comphenix.packetwrapper.old;


import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.UUID;

public class WrapperPlayServerNamedEntitySpawn
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.NAMED_ENTITY_SPAWN;

    public WrapperPlayServerNamedEntitySpawn() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerNamedEntitySpawn(PacketContainer packet) {
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

    public UUID getPlayerUUID() {
        return (UUID)this.handle.getSpecificModifier(UUID.class).read(0);
    }

    public void setPlayerUUID(UUID value) {
        this.handle.getSpecificModifier(UUID.class).write(0, value);
    }

    public Vector getPosition() {
        return new Vector(this.getX(), this.getY(), this.getZ());
    }

    public void setPosition(Vector position) {
        this.setX(position.getX());
        this.setY(position.getY());
        this.setZ(position.getZ());
    }

    public double getX() {
        return (double)((Integer)this.handle.getIntegers().read(1)).intValue() / 32.0;
    }

    public void setX(double value) {
        this.handle.getIntegers().write(1, ((int)Math.floor(value * 32.0)));
    }

    public double getY() {
        return (double)((Integer)this.handle.getIntegers().read(2)).intValue() / 32.0;
    }

    public void setY(double value) {
        this.handle.getIntegers().write(2, ((int)Math.floor(value * 32.0)));
    }

    public double getZ() {
        return (double)((Integer)this.handle.getIntegers().read(3)).intValue() / 32.0;
    }

    public void setZ(double value) {
        this.handle.getIntegers().write(3, ((int)Math.floor(value * 32.0)));
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

    public int getCurrentItem() {
        return (Integer)this.handle.getIntegers().read(4);
    }

    public void setCurrentItem(int value) {
        this.handle.getIntegers().write(4, value);
    }

    public WrappedDataWatcher getMetadata() {
        return (WrappedDataWatcher)this.handle.getDataWatcherModifier().read(0);
    }

    public void setMetadata(WrappedDataWatcher value) {
            this.handle.getDataWatcherModifier().write(0, value);

    }
}


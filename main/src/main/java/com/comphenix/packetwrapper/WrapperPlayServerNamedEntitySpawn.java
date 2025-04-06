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
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.UUID;

import me.joaomanoel.d4rkk.dev.replay.VersionUtil;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

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
        return (UUID)this.handle.getUUIDs().read(0);
    }

    public void setPlayerUUID(UUID value) {
        this.handle.getUUIDs().write(0, value);
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
        return (Double)this.handle.getDoubles().read(0);
    }

    public void setX(double value) {
        this.handle.getDoubles().write(0, value);
    }

    public double getY() {
        return (Double)this.handle.getDoubles().read(1);
    }

    public void setY(double value) {
        this.handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return (Double)this.handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        this.handle.getDoubles().write(2, value);
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

    public WrappedDataWatcher getMetadata() {
        return (WrappedDataWatcher)this.handle.getDataWatcherModifier().read(0);
    }

    public void setMetadata(WrappedDataWatcher value) {
        if (me.joaomanoel.d4rkk.dev.replay.VersionUtil.isBelow(VersionUtil.VersionEnum.V1_14)) {
            this.handle.getDataWatcherModifier().write(0, value);
        }
    }
}


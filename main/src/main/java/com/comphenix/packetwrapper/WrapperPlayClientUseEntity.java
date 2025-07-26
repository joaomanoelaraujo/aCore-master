/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.util.Vector
 */
package com.comphenix.packetwrapper;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class WrapperPlayClientUseEntity
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.USE_ENTITY;

    public WrapperPlayClientUseEntity() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientUseEntity(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getTargetID() {
        return this.handle.getIntegers().read(0);
    }

    public Entity getTarget(World world) {
        return this.handle.getEntityModifier(world).read(0);
    }

    public Entity getTarget(PacketEvent event) {
        return this.getTarget(event.getPlayer().getWorld());
    }

    public void setTargetID(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public EnumWrappers.EntityUseAction getType() {

        return this.handle.getEntityUseActions().read(0);
    }

    public void setType(EnumWrappers.EntityUseAction value) {
        this.handle.getEntityUseActions().write(0, value);
    }

    public Vector getTargetVector() {
        return this.handle.getVectors().read(0);
    }

    public void setTargetVector(Vector value) {
        this.handle.getVectors().write(0, value);
    }
}


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

public class WrapperPlayServerRelEntityMove
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.REL_ENTITY_MOVE;

    public WrapperPlayServerRelEntityMove() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerRelEntityMove(PacketContainer packet) {
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

    public int getDx() {
        return (Integer)this.handle.getIntegers().read(1);
    }

    public void setDx(int value) {
        this.handle.getIntegers().write(1, value);
    }

    public int getDy() {
        return (Integer)this.handle.getIntegers().read(2);
    }

    public void setDy(int value) {
        this.handle.getIntegers().write(2, value);
    }

    public int getDz() {
        return (Integer)this.handle.getIntegers().read(3);
    }

    public void setDz(int value) {
        this.handle.getIntegers().write(3, value);
    }

    public boolean getOnGround() {
        return (Boolean)this.handle.getBooleans().read(0);
    }

    public void setOnGround(boolean value) {
        this.handle.getBooleans().write(0, value);
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerAction
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayClientEntityAction
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.ENTITY_ACTION;

    public WrapperPlayClientEntityAction() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientEntityAction(PacketContainer packet) {
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

    public EnumWrappers.PlayerAction getAction() {
        return (EnumWrappers.PlayerAction)this.handle.getPlayerActions().read(0);
    }

    public void setAction(EnumWrappers.PlayerAction value) {
        this.handle.getPlayerActions().write(0, value);
    }

    public int getJumpBoost() {
        return (Integer)this.handle.getIntegers().read(1);
    }

    public void setJumpBoost(int value) {
        this.handle.getIntegers().write(1, value);
    }
}


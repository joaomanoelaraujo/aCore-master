/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.EntityType
 */
package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.entity.EntityType;

public class ProjectileData
extends PacketData {
    private static final long serialVersionUID = 8320803760880960739L;
    private LocationData spawn;
    private LocationData velocity;
    private EntityType type;

    public ProjectileData(LocationData spawn, LocationData velocity, EntityType type) {
        this.spawn = spawn;
        this.velocity = velocity;
        this.type = type;
    }

    public LocationData getSpawn() {
        return this.spawn;
    }

    public LocationData getVelocity() {
        return this.velocity;
    }

    public EntityType getType() {
        return this.type;
    }
}


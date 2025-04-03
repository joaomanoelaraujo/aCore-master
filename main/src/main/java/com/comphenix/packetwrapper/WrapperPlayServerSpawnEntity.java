/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.injector.PacketConstructor
 *  com.comphenix.protocol.reflect.IntEnum
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 */
package com.comphenix.packetwrapper;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.reflect.IntEnum;
import java.util.UUID;

import me.joaomanoel.d4rkk.dev.replay.VersionUtil;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerSpawnEntity
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY;
    private static PacketConstructor entityConstructor;

    public WrapperPlayServerSpawnEntity() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSpawnEntity(PacketContainer packet) {
        super(packet, TYPE);
    }

    public WrapperPlayServerSpawnEntity(Entity entity, int type, int objectData) {
        super(WrapperPlayServerSpawnEntity.fromEntity(entity, type, objectData), TYPE);
    }

    private static PacketContainer fromEntity(Entity entity, int type, int objectData) {
        if (entityConstructor == null) {
            entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, new Object[]{entity, type, objectData});
        }
        return entityConstructor.createPacket(new Object[]{entity, type, objectData});
    }

    public int getEntityID() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public Entity getEntity(World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }

    public void setEntityID(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public UUID getUniqueId() {
        return (UUID)this.handle.getUUIDs().read(0);
    }

    public void setUniqueId(UUID value) {
        if (!VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            this.handle.getUUIDs().write(0, value);
        }
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

    public double getOptionalSpeedX() {
        return (double)((Integer)this.handle.getIntegers().read(1)).intValue() / 8000.0;
    }

    public void setOptionalSpeedX(double value) {
        this.handle.getIntegers().write(1, ((int)(value * 8000.0)));
    }

    public double getOptionalSpeedY() {
        return (double)((Integer)this.handle.getIntegers().read(2)).intValue() / 8000.0;
    }

    public void setOptionalSpeedY(double value) {
        this.handle.getIntegers().write(2, ((int)(value * 8000.0)));
    }

    public double getOptionalSpeedZ() {
        return (double)((Integer)this.handle.getIntegers().read(3)).intValue() / 8000.0;
    }

    public void setOptionalSpeedZ(double value) {
        this.handle.getIntegers().write(3, ((int)(value * 8000.0)));
    }

    public float getPitch() {
        return (float)((Integer)this.handle.getIntegers().read(4)).intValue() * 360.0f / 256.0f;
    }

    public void setPitch(float value) {
        this.handle.getIntegers().write(4, ((int)(value * 256.0f / 360.0f)));
    }

    public float getYaw() {
        return (float)((Integer)this.handle.getIntegers().read(5)).intValue() * 360.0f / 256.0f;
    }

    public void setYaw(float value) {
        this.handle.getIntegers().write(5, ((int)(value * 256.0f / 360.0f)));
    }

    public int getType() {
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_19)) {
            return 0;
        }
        return (Integer)this.handle.getIntegers().read(6);
    }

    public void setType(int value) {
        this.handle.getIntegers().write(6, value);
    }

    public int getObjectData() {
        if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_14, VersionUtil.VersionEnum.V1_18)) {
            return (Integer)this.handle.getIntegers().read(6);
        }
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_19)) {
            return (Integer)this.handle.getIntegers().read(4);
        }
        return (Integer)this.handle.getIntegers().read(7);
    }

    public void setObjectData(int value) {
        if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_14, VersionUtil.VersionEnum.V1_18)) {
            this.handle.getIntegers().write(6, value);
            return;
        }
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_19)) {
            this.handle.getIntegers().write(4, value);
            return;
        }
        this.handle.getIntegers().write(7, value);
    }

    public static class ObjectTypes
    extends IntEnum {
        public static final int BOAT = 1;
        public static final int ITEM_STACK = 2;
        public static final int AREA_EFFECT_CLOUD = 3;
        public static final int MINECART = 10;
        public static final int ACTIVATED_TNT = 50;
        public static final int ENDER_CRYSTAL = 51;
        public static final int TIPPED_ARROW_PROJECTILE = 60;
        public static final int SNOWBALL_PROJECTILE = 61;
        public static final int EGG_PROJECTILE = 62;
        public static final int GHAST_FIREBALL = 63;
        public static final int BLAZE_FIREBALL = 64;
        public static final int THROWN_ENDERPEARL = 65;
        public static final int WITHER_SKULL_PROJECTILE = 66;
        public static final int SHULKER_BULLET = 67;
        public static final int FALLING_BLOCK = 70;
        public static final int ITEM_FRAME = 71;
        public static final int EYE_OF_ENDER = 72;
        public static final int THROWN_POTION = 73;
        public static final int THROWN_EXP_BOTTLE = 75;
        public static final int FIREWORK_ROCKET = 76;
        public static final int LEASH_KNOT = 77;
        public static final int ARMORSTAND = 78;
        public static final int FISHING_FLOAT = 90;
        public static final int SPECTRAL_ARROW = 91;
        public static final int DRAGON_FIREBALL = 93;
        private static ObjectTypes INSTANCE = new ObjectTypes();

        public static ObjectTypes getInstance() {
            return INSTANCE;
        }
    }
}


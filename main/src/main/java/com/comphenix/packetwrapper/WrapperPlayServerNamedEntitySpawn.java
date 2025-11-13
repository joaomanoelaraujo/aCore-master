package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.UUID;

public class WrapperPlayServerNamedEntitySpawn extends AbstractPacket {

    private static final PacketType TYPE = getPacketType();

    private static PacketType getPacketType() {
        try {
            Field field = PacketType.Play.Server.class.getField("NAMED_ENTITY_SPAWN");
            return (PacketType) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            try {
                Field field = PacketType.Play.Server.class.getField("SPAWN_PLAYER");
                return (PacketType) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new RuntimeException("Unable to find player spawn packet type", ex);
            }
        }
    }

    public WrapperPlayServerNamedEntitySpawn() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerNamedEntitySpawn(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityID() {
        return this.handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public Entity getEntity(World world) {
        return this.handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }

    public UUID getPlayerUUID() {
        return this.handle.getUUIDs().read(0);
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
        return this.handle.getDoubles().read(0);
    }

    public void setX(double value) {
        this.handle.getDoubles().write(0, value);
    }

    public double getY() {
        return this.handle.getDoubles().read(1);
    }

    public void setY(double value) {
        this.handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return this.handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        this.handle.getDoubles().write(2, value);
    }

    public float getYaw() {
        return (float) this.handle.getBytes().read(0).byteValue() * 360.0f / 256.0f;
    }

    public void setYaw(float value) {
        this.handle.getBytes().write(0, ((byte) (value * 256.0f / 360.0f)));
    }

    public float getPitch() {
        return (float) this.handle.getBytes().read(1).byteValue() * 360.0f / 256.0f;
    }

    public void setPitch(float value) {
        this.handle.getBytes().write(1, ((byte) (value * 256.0f / 360.0f)));
    }

    public WrappedDataWatcher getMetadata() {
        return this.handle.getDataWatcherModifier().read(0);
    }

    public void setMetadata(WrappedDataWatcher value) {
        this.handle.getDataWatcherModifier().write(0, value);
    }
}

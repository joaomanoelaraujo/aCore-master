package me.joaomanoel.d4rkk.dev.replay;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class WrapperPlayServerSpawnEntity {

    private final PacketContainer handle;

    public WrapperPlayServerSpawnEntity() {
        this.handle = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSpawnEntity(PacketContainer packet) {
        this.handle = packet;
    }

    public PacketContainer getHandle() {
        return this.handle;
    }

    public int getEntityID() {
        return this.handle.getIntegers().readSafely(0);
    }

    public void setEntityID(int value) {
        this.handle.getIntegers().writeSafely(0, value);
    }

    public UUID getUniqueId() {
        return this.handle.getUUIDs().readSafely(0);
    }

    public void setUniqueId(UUID value) {
        this.handle.getUUIDs().writeSafely(0, value);
    }

    public int getType() {
        return this.handle.getIntegers().readSafely(1);
    }

    public void setType(int value) {
        this.handle.getIntegers().writeSafely(1, value);
    }

    public double getX() {
        Integer val = this.handle.getIntegers().readSafely(1);
        if (val == null) {
            val = this.handle.getIntegers().readSafely(2);
        }
        return val != null ? val / 32.0D : 0.0D;
    }

    public void setX(double value) {
        this.handle.getIntegers().writeSafely(2, (int) (value * 32.0D));
    }

    public double getY() {
        Integer val = this.handle.getIntegers().readSafely(2);
        if (val == null) {
            val = this.handle.getIntegers().readSafely(3);
        }
        return val != null ? val / 32.0D : 0.0D;
    }

    public void setY(double value) {
        this.handle.getIntegers().writeSafely(3, (int) (value * 32.0D));
    }

    public double getZ() {
        Integer val = this.handle.getIntegers().readSafely(3);
        if (val == null) {
            val = this.handle.getIntegers().readSafely(4);
        }
        return val != null ? val / 32.0D : 0.0D;
    }

    public void setZ(double value) {
        this.handle.getIntegers().writeSafely(4, (int) (value * 32.0D));
    }

    public float getPitch() {
        Byte b = this.handle.getBytes().readSafely(0);
        return b != null ? (float) b * 360.0F / 256.0F : 0.0F;
    }

    public void setPitch(float value) {
        this.handle.getBytes().writeSafely(0, (byte) (value * 256.0F / 360.0F));
    }

    public float getYaw() {
        Byte b = this.handle.getBytes().readSafely(1);
        return b != null ? (float) b * 360.0F / 256.0F : 0.0F;
    }

    public void setYaw(float value) {
        this.handle.getBytes().writeSafely(1, (byte) (value * 256.0F / 360.0F));
    }

    public int getObjectData() {
        Integer val = this.handle.getIntegers().readSafely(5);
        if (val == null) {
            val = this.handle.getIntegers().readSafely(10);
        }
        return val != null ? val : 0;
    }

    public void setObjectData(int value) {
        this.handle.getIntegers().writeSafely(10, value);
    }

    public double getOptionalSpeedX() {
        Integer val = this.handle.getIntegers().readSafely(6);
        if (val == null) {
            val = this.handle.getIntegers().readSafely(7);
        }
        return val != null ? val / 8000.0D : 0.0D;
    }

    public void setOptionalSpeedX(double value) {
        this.handle.getIntegers().writeSafely(7, (int) (value * 8000.0D));
    }

    public double getOptionalSpeedY() {
        Integer val = this.handle.getIntegers().readSafely(7);
        if (val == null) {
            val = this.handle.getIntegers().readSafely(8);
        }
        return val != null ? val / 8000.0D : 0.0D;
    }

    public void setOptionalSpeedY(double value) {
        this.handle.getIntegers().writeSafely(8, (int) (value * 8000.0D));
    }

    public double getOptionalSpeedZ() {
        Integer val = this.handle.getIntegers().readSafely(8);
        if (val == null) {
            val = this.handle.getIntegers().readSafely(9);
        }
        return val != null ? val / 8000.0D : 0.0D;
    }

    public void setOptionalSpeedZ(double value) {
        this.handle.getIntegers().writeSafely(9, (int) (value * 8000.0D));
    }

    public Entity getEntity(World world) {
        try {
            return this.handle.getEntityModifier(world).read(0);
        } catch (Exception e) {
            return null;
        }
    }

    public EntityType getEntityType() {
        try {
            return this.handle.getEntityTypeModifier().read(0);
        } catch (Exception e) {
            return null;
        }
    }
}

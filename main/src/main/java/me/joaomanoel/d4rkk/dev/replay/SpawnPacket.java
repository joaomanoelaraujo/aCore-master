/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.WrappedDataWatcher
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.UUID;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnPacket {
    private WrapperPlayServerSpawnEntityLiving spawnEntityLiving;
    private WrapperPlayServerSpawnEntity spawnEntity;

    public SpawnPacket(WrapperPlayServerSpawnEntityLiving spawnEntityLiving) {
        this.spawnEntityLiving = spawnEntityLiving;
    }

    public SpawnPacket(WrapperPlayServerSpawnEntity spawnEntity) {
        this.spawnEntity = spawnEntity;
    }

    public void setEntityID(int id) {
        if (this.isOld()) {
            this.spawnEntityLiving.setEntityID(id);
        } else {
            this.spawnEntity.setEntityID(id);
        }
    }

    public void setType(EntityType type) {
        if (this.isOld()) {
            this.spawnEntityLiving.setType(type);
        } else {
            this.spawnEntity.getHandle().getEntityTypeModifier().write(0, type);
        }
    }

    public void setMetadata(WrappedDataWatcher data) {
        if (this.isOld()) {
            this.spawnEntityLiving.setMetadata(data);
        }
    }

    public void setX(double x) {
        if (this.isOld()) {
            this.spawnEntityLiving.setX(x);
        } else {
            this.spawnEntity.setX(x);
        }
    }

    public void setY(double y) {
        if (this.isOld()) {
            this.spawnEntityLiving.setY(y);
        } else {
            this.spawnEntity.setY(y);
        }
    }

    public void setZ(double z) {
        if (this.isOld()) {
            this.spawnEntityLiving.setZ(z);
        } else {
            this.spawnEntity.setZ(z);
        }
    }

    public void setYaw(float yaw) {
        if (this.isOld()) {
            this.spawnEntityLiving.setYaw(yaw);
        }
    }

    public void setPitch(float pitch) {
        if (this.isOld()) {
            this.spawnEntityLiving.setPitch(pitch);
        }
    }

    public void setUniqueId(UUID uuid) {
        if (this.isOld()) {
            this.spawnEntityLiving.setUniqueId(uuid);
        } else {
            this.spawnEntity.setUniqueId(uuid);
        }
    }

    public void sendPacket(Player player) {
        if (this.isOld()) {
            this.spawnEntityLiving.sendPacket(player);
        } else {
            this.spawnEntity.sendPacket(player);
        }
    }

    public boolean isOld() {
        return this.spawnEntityLiving != null;
    }
}


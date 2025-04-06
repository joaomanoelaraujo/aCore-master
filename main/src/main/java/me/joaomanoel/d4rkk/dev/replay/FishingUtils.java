/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.EntityType
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntity;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class FishingUtils {
    public static com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity createHookPacket(FishingData fishing, int throwerID, int entID) {
        Location loc = LocationData.toLocation(fishing.getLocation());
        com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity packet = new com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity();
        packet.setEntityID(entID);
        if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_13)) {
            packet.setObjectData(throwerID);
            packet.setType(90);
        }
        packet.setUniqueId(UUID.randomUUID());
        packet.setOptionalSpeedX(fishing.getX());
        packet.setOptionalSpeedY(fishing.getY());
        packet.setOptionalSpeedZ(fishing.getZ());
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14)) {
            packet.setObjectData(throwerID);
            packet.getHandle().getEntityTypeModifier().write(0, EntityType.FISHING_HOOK);
        }
        packet.setX(loc.getX());
        packet.setY(loc.getY());
        packet.setZ(loc.getZ());
        return packet;
    }

    public static WrapperPlayServerSpawnEntity createHookPacketOld(FishingData fishing, int throwerID, int entID) {
        Location loc = LocationData.toLocation(fishing.getLocation());
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity();
        packet.setEntityID(entID);
        packet.setObjectData(throwerID);
        packet.setType(90);
        packet.setOptionalSpeedX(fishing.getX());
        packet.setOptionalSpeedY(fishing.getY());
        packet.setOptionalSpeedZ(fishing.getZ());
        packet.setX(loc.getX());
        packet.setY(loc.getY());
        packet.setZ(loc.getZ());
        packet.setPitch(loc.getPitch());
        packet.setYaw(loc.getYaw());
        return packet;
    }
}


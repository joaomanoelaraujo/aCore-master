package me.joaomanoel.d4rkk.dev.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.Collection;
import java.util.Collections;


public interface IArmorStand {

    int getEntityId();

    Location getLocation();

    void spawnFor(Collection<Player> viewers);

    default void spawnFor(Player viewer) {
        spawnFor(Collections.singletonList(viewer));
    }

    void destroyFor(Collection<Player> viewers);

    default void destroyFor(Player viewer) {
        destroyFor(Collections.singletonList(viewer));
    }

    void killEntity();

    void teleport(Location location);

    void setItemInHand(ItemStack item);
    void setHelmet(ItemStack item);
    void setChestplate(ItemStack item);
    void setLeggings(ItemStack item);
    void setBoots(ItemStack item);

    void setInvisible(boolean invisible);
    void setSmall(boolean small);
    void setGravity(boolean gravity);
    void setArms(boolean arms);
    void setMarker(boolean marker);
    void setBasePlate(boolean basePlate);
    void setCustomName(String name);
    void setCustomNameVisible(boolean visible);

    void setHeadPose(EulerAngle pose);
    void setBodyPose(EulerAngle pose);
    void setLeftArmPose(EulerAngle pose);
    void setRightArmPose(EulerAngle pose);
    void setLeftLegPose(EulerAngle pose);
    void setRightLegPose(EulerAngle pose);


    void updateMetadata();
}
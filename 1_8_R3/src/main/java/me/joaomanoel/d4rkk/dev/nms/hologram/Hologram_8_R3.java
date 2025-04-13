package me.joaomanoel.d4rkk.dev.nms.hologram;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.Entity;

import java.util.Objects;

public class Hologram_8_R3 extends CraftArmorStand implements HologramEntity {

    public Hologram_8_R3(Location location) {
        super((CraftServer) Bukkit.getServer(), new EntityArmorStand(((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(), location.getX(), location.getY(), location.getZ()));
        this.setVisible(false);
        this.setGravity(false);
        this.setMarker(false);
        this.setCustomNameVisible(false);
        this.setBasePlate(false);
        this.setArms(false);
        this.setSmall(false);
    }

    @Override
    public void damage(double amount) {}

    @Override
    public void damage(double amount, Entity source) {}

    @Override
    public void spawn(String content) {
        this.setCustomName(content);
        this.setCustomNameVisible(true);
    }

    @Override
    public void kill() {
        this.remove();
    }

    @Override
    public void updateContent(String content) {
        this.setCustomName(content);
    }

}

package me.joaomanoel.d4rkk.dev.nms.hologram;

import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftArmorStand;

import java.util.Objects;


public class Hologram_20_R2 extends CraftArmorStand implements HologramEntity {

    public Hologram_20_R2(Location location) {
        super((CraftServer) Bukkit.getServer(), new EntityArmorStand(((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(), location.getX(), location.getY(), location.getZ()));
        this.setVisible(false);
        this.setGravity(false);
        this.setMarker(false);
        this.setCustomNameVisible(false);
        this.setBasePlate(false);
        this.setInvulnerable(true);
        this.setArms(false);
        this.setAI(false);
        this.setSmall(false);
    }

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

package me.joaomanoel.d4rkk.dev.nms.v1_8_R3.entity;

import me.joaomanoel.d4rkk.dev.libraries.holograms.api.HologramLine;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IArmorStand;
import me.joaomanoel.d4rkk.dev.nms.v1_8_R3.utils.NullBoundingBox;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Collection;

public class EntityArmorStand extends net.minecraft.server.v1_8_R3.EntityArmorStand implements IArmorStand {
  
  private final HologramLine line;
  
  public EntityArmorStand(World world, HologramLine line) {
    super(world);
    setInvisible(true);
    setSmall(true);
    setArms(false);
    setGravity(true);
    setBasePlate(true);
    this.line = line;
    try {
      Field field = net.minecraft.server.v1_8_R3.EntityArmorStand.class.getDeclaredField("bi");
      field.setAccessible(true);
      field.set(this, 2147483647);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    a(new NullBoundingBox());
  }
  
  private static double square(double num) {
    return num * num;
  }
  
  public boolean c(NBTTagCompound nbttagcompound) {
    return false;
  }
  
  public boolean d(NBTTagCompound nbttagcompound) {
    return false;
  }
  
  public void e(NBTTagCompound nbttagcompound) {
  }
  
  public void f(NBTTagCompound nbttagcompound) {
  }
  
  public boolean isInvulnerable(DamageSource source) {
    return true;
  }
  
  public void setCustomName(String customName) {
  }
  
  public void setCustomNameVisible(boolean visible) {
  }
  
  public boolean a(EntityHuman human, Vec3D vec3d) {
    return true;
  }
  
  public void t_() {
    this.ticksLived = 0;
    if (line == null) {
      this.dead = true;
    }
    super.t_();
  }
  
  public void makeSound(String sound, float f1, float f2) {
  }
  
  public CraftEntity getBukkitEntity() {
    if (this.bukkitEntity == null) {
      this.bukkitEntity = new CraftArmorStand(world.getServer(), this);
    }
    return this.bukkitEntity;
  }
  
  public void die() {
    super.die();
  }
  
  @Override
  public void setLocation(double x, double y, double z) {
    super.setPosition(x, y, z);
    
    PacketPlayOutEntityTeleport teleportPacket =
        new PacketPlayOutEntityTeleport(getId(), MathHelper.floor(this.locX * 32.0D), MathHelper.floor(this.locY * 32.0D), MathHelper.floor(this.locZ * 32.0D),
            (byte) (int) (this.yaw * 256.0F / 360.0F), (byte) (int) (this.pitch * 256.0F / 360.0F), this.onGround);
    
    for (EntityHuman obj : world.players) {
      if (obj instanceof EntityPlayer) {
        EntityPlayer nmsPlayer = (EntityPlayer) obj;
        
        double distanceSquared = square(nmsPlayer.locX - this.locX) + square(nmsPlayer.locZ - this.locZ);
        if (distanceSquared < 8192.0 && nmsPlayer.playerConnection != null) {
          nmsPlayer.playerConnection.sendPacket(teleportPacket);
        }
      }
    }
  }
  
  @Override
  public boolean isDead() {
    return dead;
  }
  
  @Override
  public void killEntity() {
    die();
  }
  
  @Override
  public int getId() {
    return super.getId();
  }
  
  @Override
  public void setName(String name) {
    if (name != null && name.length() > 300) {
      name = name.substring(0, 300);
    }
    super.setCustomName(name == null ? "" : name);
    super.setCustomNameVisible(name != null && !name.equals(""));
  }
  
  @Override
  public ArmorStand getEntity() {
    return (ArmorStand) getBukkitEntity();
  }
  
  @Override
  public HologramLine getLine() {
    return line;
  }

  @Override
  public void remove() {

  }

  public static class CraftArmorStand extends org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand implements IArmorStand {
    
    public CraftArmorStand(CraftServer server, EntityArmorStand entity) {
      super(server, entity);
    }
    
    @Override
    public int getId() {
      return entity.getId();
    }
    
    @Override
    public void setName(String text) {
      ((EntityArmorStand) entity).setName(text);
    }
    
    @Override
    public void killEntity() {
      ((EntityArmorStand) entity).killEntity();
    }
    
    @Override
    public HologramLine getLine() {
      return ((EntityArmorStand) entity).getLine();
    }
    
    @Override
    public ArmorStand getEntity() {
      return this;
    }
    
    @Override
    public void setLocation(double x, double y, double z) {
      ((EntityArmorStand) entity).setLocation(x, y, z);
    }
    
    public void remove() {
    }
    
    public void setArms(boolean arms) {
    }
    
    public void setBasePlate(boolean basePlate) {
    }
    
    public void setBodyPose(EulerAngle pose) {
      ((EntityArmorStand) entity).setBodyPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }
    
    public void setGravity(boolean gravity) {
    }
    
    public void setHeadPose(EulerAngle pose) {
      ((EntityArmorStand) entity).setHeadPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }
    
    public void setLeftArmPose(EulerAngle pose) {
      ((EntityArmorStand) entity).setLeftArmPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }
    
    public void setLeftLegPose(EulerAngle pose) {
      ((EntityArmorStand) entity).setLeftLegPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }
    
    public void setRightArmPose(EulerAngle pose) {
      ((EntityArmorStand) entity).setRightArmPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }
    
    public void setRightLegPose(EulerAngle pose) {
      ((EntityArmorStand) entity).setRightLegPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }
    
    public void setVisible(boolean visible) {
    }
    
    public boolean addPotionEffect(PotionEffect effect) {
      return false;
    }
    
    public boolean addPotionEffect(PotionEffect effect, boolean param) {
      return false;
    }
    
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
      return false;
    }
    
    public void setRemoveWhenFarAway(boolean remove) {
    }
    
    public void setVelocity(Vector vel) {
    }
    
    public boolean teleport(Location loc) {
      return false;
    }
    
    public boolean teleport(Entity entity) {
      return false;
    }
    
    public boolean teleport(Location loc, PlayerTeleportEvent.TeleportCause cause) {
      return false;
    }
    
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause cause) {
      return false;
    }
    
    public void setFireTicks(int ticks) {
    }
    
    public boolean setPassenger(Entity entity) {
      return false;
    }
    
    public boolean eject() {
      return false;
    }
    
    public boolean leaveVehicle() {
      return false;
    }
    
    public void playEffect(EntityEffect effect) {
    }
    
    public void setCustomName(String name) {
    }
    
    public void setCustomNameVisible(boolean flag) {
    }
  }
}

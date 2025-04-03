package hypedmc.com.br.maincore.v1_20_R3.entity;

import me.joaomanoel.d4rkk.dev.libraries.holograms.api.HologramLine;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IArmorStand;
import net.minecraft.core.Vector3f;

import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;

import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

import java.lang.reflect.Field;

public class EntityArmorStand extends EntityArmorStand implements IArmorStand {

  private final HologramLine line;

  public EntityArmorStand(World world, HologramLine line) {
    super(world);
    setInvisible(true);
    setSmall(true);
    setArms(false);
    setNoGravity(true); // Ajustado para refletir a configuração da gravidade
    setBasePlate(false);
    this.line = line;

    try {
      Field field = EntityArmorStand.class.getDeclaredField("bi");
      field.setAccessible(true);
      field.set(this, Integer.MAX_VALUE);  // Evita que a entidade seja removida automaticamente
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static double square(double num) {
    return num * num;
  }

  @Override
  public boolean isInvulnerable(DamageSource source) {
    return true;
  }

  @Override
  public void setCustomName(String customName) {
    super.setCustomName(customName);
  }

  @Override
  public void setCustomNameVisible(boolean visible) {
    super.setCustomNameVisible(visible);
  }

  @Override
  public boolean a(EntityHuman human, Vec3D vec3d) {
    return true;
  }

  @Override
  public void tick() {
    this.ticksLived = 0;
    if (line == null) {
      this.discard(); // Marca a entidade para remoção
    }
    super.tick();
  }

  @Override
  public void makeSound(String sound, float f1, float f2) {
    // Bloqueia sons de interação
  }

  @Override
  public CraftEntity getBukkitEntity() {
    if (this.bukkitEntity == null) {
      this.bukkitEntity = new CraftArmorStand(world.getServer(), this);
    }
    return this.bukkitEntity;
  }

  @Override
  public void discard() {
    super.discard();
  }

  @Override
  public void setLocation(double x, double y, double z) {
    super.setPos(x, y, z);

    PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);
    for (EntityPlayer player : ((WorldServer) this.level).players) {
      player.connection.send(teleportPacket);
    }
  }

  @Override
  public boolean isDead() {
    return this.isRemoved();
  }

  @Override
  public void killEntity() {
    this.discard();
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
    super.setCustomName(name == null ? null : name);
    super.setCustomNameVisible(name != null && !name.equals(""));
  }

  @Override
  public ArmorStand getEntity() {
    return (ArmorStand) this.getBukkitEntity();
  }

  @Override
  public HologramLine getLine() {
    return this.line;
  }

  public static class CraftArmorStand extends CraftArmorStand implements IArmorStand {

    public CraftArmorStand(CraftServer server, EntityArmorStand entity) {
      super(server, entity);
    }

    @Override
    public int getId() {
      return this.getHandle().getId();
    }

    @Override
    public void setName(String text) {
      ((EntityArmorStand) this.getHandle()).setName(text);
    }

    @Override
    public void killEntity() {
      ((EntityArmorStand) this.getHandle()).killEntity();
    }

    @Override
    public HologramLine getLine() {
      return ((EntityArmorStand) this.getHandle()).getLine();
    }

    @Override
    public ArmorStand getEntity() {
      return this;
    }

    @Override
    public void setLocation(double x, double y, double z) {
      ((EntityArmorStand) this.getHandle()).setLocation(x, y, z);
    }

    @Override
    public boolean isDead() {
      return false;
    }

    @Override
    public void remove() {
      // Evita remoção manual
    }

    @Override
    public void setArms(boolean arms) {
      // Não implementado
    }

    @Override
    public void setBasePlate(boolean basePlate) {
      // Não implementado
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
      ((EntityArmorStand) this.getHandle()).setBodyPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }

    @Override
    public void setGravity(boolean gravity) {
      ((EntityArmorStand) this.getHandle()).setNoGravity(!gravity);
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
      ((EntityArmorStand) this.getHandle()).setHeadPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
      ((EntityArmorStand) this.getHandle()).setLeftArmPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
      ((EntityArmorStand) this.getHandle()).setLeftLegPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
      ((EntityArmorStand) this.getHandle()).setRightArmPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
      ((EntityArmorStand) this.getHandle()).setRightLegPose(new Vector3f((float) pose.getX(), (float) pose.getY(), (float) pose.getZ()));
    }

    @Override
    public void setVisible(boolean visible) {
      ((EntityArmorStand) this.getHandle()).setInvisible(!visible);
    }
  }
}

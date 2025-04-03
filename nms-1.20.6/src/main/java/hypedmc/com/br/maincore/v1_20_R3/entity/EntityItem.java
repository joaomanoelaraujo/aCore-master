package hypedmc.com.br.maincore.v1_20_R3.entity;

import me.joaomanoel.d4rkk.dev.libraries.holograms.HologramLibrary;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.HologramLine;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;

import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;

import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class EntityItem extends EntityItem implements IItem {

  private final HologramLine line;

  public EntityItem(World world, HologramLine line) {
    super(world);
    this.pickupDelay = 0; // Permitir pickup imediato
    this.line = line;
  }

  @Override
  public void tick() {
    this.ticksLived = 0; // Evita que o item expire
    super.tick();
  }

  @Override
  public void inactiveTick() {
    this.ticksLived = 0; // Garante que o item não será removido
  }

  @Override
  public void playerTouch(net.minecraft.world.entity.player.EntityHuman entityHuman) {
    if (entityHuman.getY() < this.getY() - 1.5 || entityHuman.getY() > this.getY() + 1.0) {
      return;
    }

    if (entityHuman instanceof net.minecraft.server.level.EntityPlayer && line.getPickupHandler() != null) {
      line.getPickupHandler().onPickup((Player) entityHuman.getBukkitEntity());
    }
  }

  @Override
  public void saveData(NBTTagCompound nbttagcompound) {
    // Impede que dados sejam salvos
  }

  @Override
  public boolean save(NBTTagCompound nbttagcompound) {
    return false; // Impede a persistência
  }

  @Override
  public boolean load(NBTTagCompound nbttagcompound) {
    return false;
  }

  @Override
  public void loadData(NBTTagCompound nbttagcompound) {
    // Não faz nada
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    return true; // Torna o item invulnerável
  }

  @Override
  public void discard() {
    // Não permite que o item seja descartado
  }

  @Override
  public boolean isAlive() {
    return false; // Define que o item não está "vivo"
  }

  @Override
  public CraftEntity getBukkitEntity() {
    if (this.bukkitEntity == null) {
      this.bukkitEntity = new CraftItem((CraftServer) Bukkit.getServer(), this);
    }
    return this.bukkitEntity;
  }

  @Override
  public void setPassengerOf(Entity entity) {
    if (entity == null) return;

    net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
    try {
      Field pitchDelta = net.minecraft.world.entity.Entity.class.getDeclaredField("ar");
      pitchDelta.setAccessible(true);
      pitchDelta.set(this, 0.0);

      Field yawDelta = net.minecraft.world.entity.Entity.class.getDeclaredField("as");
      yawDelta.setAccessible(true);
      yawDelta.set(this, 0.0);
    } catch (ReflectiveOperationException ex) {
      HologramLibrary.LOGGER.log(Level.SEVERE, "Não foi possível definir o pitch e yaw: ", ex);
    }

    if (this.getVehicle() != null) {
      this.getVehicle().stopRiding();
    }

    this.startRiding(nmsEntity, true);
  }

  @Override
  public void setItemStack(org.bukkit.inventory.ItemStack item) {
    ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

    if (nmsItem == null) {
      nmsItem = new ItemStack(net.minecraft.world.item.Items.BARRIER);
    }

    if (nmsItem.getTag() == null) {
      nmsItem.setTag(new NBTTagCompound());
    }

    NBTTagCompound display = nmsItem.getTag().getCompound("display");
    if (!nmsItem.getTag().contains("display")) {
      nmsItem.getTag().put("display", display);
    }

    NBTTagList tagList = new NBTTagList();
    tagList.add(NBTTagString.a("§0" + ThreadLocalRandom.current().nextInt()));
    display.put("Lore", tagList);

    this.setItem(nmsItem);
  }

  @Override
  public void setLocation(double x, double y, double z) {
    this.setPos(x, y, z);
  }

  @Override
  public boolean isDead() {
    return this.isRemoved();
  }

  @Override
  public void killEntity() {
    this.remove(RemovalReason.KILLED);
  }

  @Override
  public Item getEntity() {
    return (Item) this.getBukkitEntity();
  }

  @Override
  public HologramLine getLine() {
    return this.line;
  }

  public static class CraftItem extends CraftItem implements IItem {

    public CraftItem(CraftServer server, EntityItem entity) {
      super(server, entity);
    }

    @Override
    public void remove() {
      // Evita remoção direta
    }

    @Override
    public void setVelocity(Vector velocity) {
      // Desativa mudança de velocidade
    }

    @Override
    public boolean teleport(Location location) {
      return false;
    }

    @Override
    public boolean teleport(Entity entity) {
      return false;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
      return false;
    }

    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause cause) {
      return false;
    }

    @Override
    public void setFireTicks(int ticks) {
      // Ignorado
    }

    @Override
    public boolean setPassenger(Entity entity) {
      return false;
    }

    @Override
    public boolean eject() {
      return false;
    }

    @Override
    public boolean leaveVehicle() {
      return false;
    }

    @Override
    public void playEffect(EntityEffect effect) {
      // Ignorado
    }

    @Override
    public void setCustomName(String name) {
      // Ignorado
    }

    @Override
    public void setCustomNameVisible(boolean visible) {
      // Ignorado
    }

    @Override
    public void setPickupDelay(int delay) {
      // Ignorado
    }

    @Override
    public void setPassengerOf(Entity entity) {
      ((EntityItem) this.getHandle()).setPassengerOf(entity);
    }

    @Override
    public void setLocation(double x, double y, double z) {
      ((EntityItem) this.getHandle()).setLocation(x, y, z);
    }

    @Override
    public void killEntity() {
      ((EntityItem) this.getHandle()).killEntity();
    }

    @Override
    public void setItemStack(org.bukkit.inventory.ItemStack stack) {
      // Ignorado
    }

    @Override
    public Item getEntity() {
      return this;
    }

    @Override
    public HologramLine getLine() {
      return ((EntityItem) this.getHandle()).getLine();
    }
  }
}

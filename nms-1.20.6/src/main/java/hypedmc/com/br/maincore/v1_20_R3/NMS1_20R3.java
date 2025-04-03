package hypedmc.com.br.maincore.v1_20_R3;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import hypedmc.com.br.maincore.v1_20_R3.entity.EntityItem;
import hypedmc.com.br.maincore.v1_20_R3.entity.EntitySlime;
import hypedmc.com.br.maincore.v1_20_R3.entity.EntityStand;
import hypedmc.com.br.maincore.v1_20_R3.entity.HumanController;
import hypedmc.com.br.maincore.v1_20_R3.utils.PlayerlistTrackerEntry;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.Hologram;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.HologramLine;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPC;
import me.joaomanoel.d4rkk.dev.libraries.npclib.api.npc.NPCAnimation;
import me.joaomanoel.d4rkk.dev.libraries.npclib.npc.EntityControllers;
import me.joaomanoel.d4rkk.dev.libraries.npclib.npc.ai.NPCHolder;
import me.joaomanoel.d4rkk.dev.libraries.npclib.npc.skin.SkinnableEntity;
import me.joaomanoel.d4rkk.dev.nms.interfaces.INMS;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IArmorStand;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.IItem;
import me.joaomanoel.d4rkk.dev.nms.interfaces.entity.ISlime;
import me.joaomanoel.d4rkk.dev.reflection.Accessors;
import me.joaomanoel.d4rkk.dev.reflection.acessors.FieldAccessor;

import me.joaomanoel.d4rkk.dev.utils.Utils;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.EntityTrackerEntry;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NMS1_20R3 implements INMS {

  private final FieldAccessor<Set> SET_TRACKERS;
  private final FieldAccessor<Map> CLASS_TO_ID, CLASS_TO_NAME;
  private final FieldAccessor<List> PATHFINDERGOAL_B, PATHFINDERGOAL_C;
  private final Map<Integer, Hologram> preHologram = new HashMap<>();

  public NMS1_20R3() {
    // Mapas de tipos de entidades
    CLASS_TO_ID = Accessors.getField(EntityTypes.class, "f", Map.class);
    CLASS_TO_NAME = Accessors.getField(EntityTypes.class, "d", Map.class);

    // Pathfinders (IA das entidades)
    PATHFINDERGOAL_B = Accessors.getField(PathfinderGoalSelector.class, 0, List.class);
    PATHFINDERGOAL_C = Accessors.getField(PathfinderGoalSelector.class, 1, List.class);

    // Rastreamento de entidades no mapa de chunk
    SET_TRACKERS = Accessors.getField(PlayerChunkMap.EntityTracker.class, "c", Set.class);

    // Configurando controlador de NPCs
    EntityControllers.registerEntityController(EntityType.PLAYER, HumanController.class);
  }


  @Override
  public void clearPathfinderGoal(Object entity) {
    if (entity instanceof org.bukkit.entity.Entity bukkitEntity) {
      entity = ((org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity) bukkitEntity).getHandle();
    }

    if (entity instanceof EntityInsentient entityInsentient) {
      PATHFINDERGOAL_B.get(entityInsentient.goalSelector).clear();
      PATHFINDERGOAL_C.get(entityInsentient.targetSelector).clear();
    }
  }

  @Override
  public void sendPacket(Player player, Object packet) {
    if (packet instanceof Packet<?>) {
      ((CraftPlayer) player).getHandle().connection.send((Packet<?>) packet);
    }
  }

  @Override
  public void sendTabListAdd(Player player, Player listPlayer) {
    EntityPlayer nmsListPlayer = ((CraftPlayer) listPlayer).getHandle();
    ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsListPlayer);
    sendPacket(player, packet);
  }

  @Override
  public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    ep.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
    ep.connection.send(new ClientboundSetTitleTextPacket(IChatBaseComponent.literal(title)));
    ep.connection.send(new ClientboundSetSubtitleTextPacket(IChatBaseComponent.literal(subtitle)));
  }

  @Override
  public void sendActionBar(Player player, String message) {
    PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"), (byte) 2);
    sendPacket(player, packet);
  }

  @Override
  public void playChestAction(Location location, boolean open) {
    BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.ENDER_CHEST, 1, open ? 1 : 0);

    for (Player players : Bukkit.getOnlinePlayers()) {
      sendPacket(players, packet);
    }
  }

  @Override
  public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    EntityPlayer ep = ((org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer) player).getHandle();
    ep.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
    ep.connection.send(new ClientboundSetTitleTextPacket(IChatBaseComponent.literal(title)));
    ep.connection.send(new ClientboundSetSubtitleTextPacket(IChatBaseComponent.literal(subtitle)));
  }

  @Override
  public void setValueAndSignature(Player player, String value, String signature) {
    GameProfile profile = ((CraftPlayer) player).getProfile();
    if (value != null && signature != null) {
      profile.getProperties().clear();
      profile.getProperties().put("textures", new Property("textures", value, signature));
    }
  }

  @Override
  public void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities) {
    SkinnableEntity[] skinnables = skinnableEntities.toArray(new SkinnableEntity[skinnableEntities.size()]);
    EntityPlayer[] entityPlayers = new EntityPlayer[skinnableEntities.size()];

    for (int i = 0; i < skinnables.length; i++) {
      entityPlayers[i] = (EntityPlayer) skinnables[i];
    }

    sendPacket(player, new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayers));
  }

  @Override
  public void sendTabListRemove(Player player, Player listPlayer) {
    sendPacket(player, new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) listPlayer).getHandle()));
  }

  @Override
  public void removeFromPlayerList(Player player) {
    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    ep.world.players.remove(ep);
  }

  @Override
  public void removeFromServerPlayerList(Player player) {
    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    ((CraftServer) Bukkit.getServer()).getHandle().players.remove(ep);
  }

  @Override
  public boolean addToWorld(World world, Entity entity, SpawnReason reason) {
    Entity nmsEntity = ((CraftEntity) entity).getHandle();
    nmsEntity.spawnIn(((CraftWorld) world).getHandle());
    return ((CraftWorld) world).getHandle().addEntity(nmsEntity, reason);
  }

  @Override
  public void removeFromWorld(Entity entity) {
    .Entity nmsEntity = ((CraftEntity) entity).getHandle();
    nmsEntity.world.removeEntity(nmsEntity);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void replaceTrackerEntry(Player player) {
    WorldServer server = ((CraftWorld) player.getWorld()).getHandle();
    EntityTrackerEntry entry = server.getTracker().trackedEntities.get(player.getEntityId());

    if (entry != null) {
      PlayerlistTrackerEntry replace = new PlayerlistTrackerEntry(entry);
      server.getTracker().trackedEntities.a(player.getEntityId(), replace);
      if (SET_TRACKERS != null) {
        Set<Object> set = SET_TRACKERS.get(server.getTracker());
        set.remove(entry);
        set.add(replace);
      }
    }
  }

  @Override
  public void sendPacket(Player player, Object packet) {
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
  }

  @Override
  public void look(Entity entity, float yaw, float pitch) {
    Entity nmsEntity = ((CraftEntity) entity).getHandle();
    if (nmsEntity != null) {
      yaw = Utils.clampYaw(yaw);
      nmsEntity.yaw = yaw;
      setHeadYaw(entity, yaw);
      nmsEntity.pitch = pitch;
    }
  }

  @Override
  public void setHeadYaw(Entity entity, float yaw) {
    Entity nmsEntity = ((CraftEntity) entity).getHandle();
    if (nmsEntity instanceof EntityLiving) {
      EntityLiving living = (EntityLiving) nmsEntity;
      yaw = Utils.clampYaw(yaw);
      living.aK = yaw;
      if (living instanceof EntityHuman) {
        living.aI = yaw;
      }
      living.aL = yaw;
    }
  }

  @Override
  public void setStepHeight(LivingEntity entity, float height) {
    ((CraftLivingEntity) entity).getHandle().S = height;
  }

  @Override
  public float getStepHeight(LivingEntity entity) {
    return ((CraftLivingEntity) entity).getHandle().S;
  }

  @Override
  public SkinnableEntity getSkinnable(Entity entity) {
    Preconditions.checkNotNull(entity);
    Entity nmsEntity = ((CraftEntity) entity).getHandle();
    if (nmsEntity instanceof SkinnableEntity) {
      return (SkinnableEntity) nmsEntity;
    }

    return null;
  }

  @Override
  public void flyingMoveLogic(LivingEntity e, float f, float f1) {
    EntityLiving entity = ((CraftLivingEntity) e).getHandle();
    if (entity.bM()) {
      if (entity.V()) {
        double d0 = entity.locY;

        float f3 = 0.8f;
        float f4 = 0.02f;
        float f2 = EnchantmentManager.b(entity);
        if (f2 > 3.0f) {
          f2 = 3.0f;
        }

        if (!entity.onGround) {
          f2 *= 0.5;
        }

        if (f2 > 0.0f) {
          f3 += (0.5460001F - f3) * f2 / 3.0f;
          f4 += (entity.bI() * 1.0f - f4) * f2 / 3.0f;
        }

        entity.a(f, f1, f4);
        entity.move(entity.motX, entity.motY, entity.motZ);
        entity.motX *= f3;
        entity.motY *= 0.800000011920929D;
        entity.motZ *= f3;
        entity.motY -= 0.02D;
        if (entity.positionChanged && entity.c(entity.motX, entity.motY + 0.6000000238418579D - entity.locY + d0, entity.motZ)) {
          entity.motY = 0.300000011920929D;
        }
      } else if (entity.ab()) {
        double d0 = entity.locY;
        entity.a(f, f1, 0.02F);
        entity.move(entity.motX, entity.motY, entity.motZ);
        entity.motX *= 0.5D;
        entity.motY *= 0.5D;
        entity.motZ *= 0.5D;
        entity.motY -= 0.02D;
        if (entity.positionChanged && entity.c(entity.motX, entity.motY + 0.6000000238418579D - entity.locY + d0, entity.motZ)) {
          entity.motY = 0.300000011920929D;
        }
      } else {
        float f5 = 0.91F;

        if (entity.onGround) {
          f5 = entity.world.getType(new BlockPosition(MathHelper.floor(entity.locX), MathHelper.floor(entity.getBoundingBox().b) - 1, MathHelper.floor(entity.locZ)))
              .getBlock().frictionFactor * 0.91F;
        }

        float f6 = 0.162771F / (f5 * f5 * f5);
        float f3;
        if (entity.onGround) {
          f3 = entity.bI() * f6;
        } else {
          f3 = entity.aM;
        }

        entity.a(f, f1, f3);
        f5 = 0.91F;
        if (entity.onGround) {
          f5 = entity.world.getType(new BlockPosition(MathHelper.floor(entity.locX), MathHelper.floor(entity.getBoundingBox().b) - 1, MathHelper.floor(entity.locZ)))
              .getBlock().frictionFactor * 0.91F;
        }

        if (entity.k_()) {
          float f4 = 0.15F;
          entity.motX = MathHelper.a(entity.motX, -f4, f4);
          entity.motZ = MathHelper.a(entity.motZ, -f4, f4);
          entity.fallDistance = 0.0F;
          if (entity.motY < -0.15D) {
            entity.motY = -0.15D;
          }

          boolean flag = entity.isSneaking() && entity instanceof EntityHuman;

          if (flag && entity.motY < 0.0D) {
            entity.motY = 0.0D;
          }
        }

        entity.move(entity.motX, entity.motY, entity.motZ);
        if ((entity.positionChanged) && (entity.k_())) {
          entity.motY = 0.2D;
        }

        if (entity.world.isClientSide && (!entity.world.isLoaded(new BlockPosition((int) entity.locX, 0, (int) entity.locZ)) || !entity.world
            .getChunkAtWorldCoords(new BlockPosition((int) entity.locX, 0, (int) entity.locZ)).o())) {
          if (entity.locY > 0.0D) {
            entity.motY = -0.1D;
          } else {
            entity.motY = 0.0D;
          }
        } else {
          entity.motY -= 0.08D;
        }

        entity.motY *= 0.9800000190734863D;
        entity.motX *= f5;
        entity.motZ *= f5;
      }
    }
  }

  @Override
  public IArmorStand createArmorStand(Location location, String name, HologramLine line) {
    EntityStand stand = new EntityStand(location, name, line);
    stand.setLocation(location.getX(), location.getY(), location.getZ());
    stand.setCustomName(name);
    addToWorld(location.getWorld(), stand.getBukkitEntity(), SpawnReason.CUSTOM);
    return stand;
  }

  public IItem createItem(Location location, ItemStack item, HologramLine line) {
    EntityItem eItem = new EntityItem(location, item, line);
    addToWorld(location.getWorld(), eItem.getBukkitEntity(), SpawnReason.CUSTOM);
    return eItem;
  }

  public ISlime createSlime(Location location, HologramLine line) {
    EntitySlime slime = new EntitySlime(location, line);
    addToWorld(location.getWorld(), slime.getBukkitEntity(), SpawnReason.CUSTOM);
    return slime;
  }
  @Override
  public Hologram getHologram(org.bukkit.entity.Entity entity) {
    if (entity == null || !(entity instanceof CraftEntity)) return null;

    Entity nmsEntity = ((CraftEntity) entity).getHandle();

    if (nmsEntity instanceof EntityStand) {
      return ((EntityStand) nmsEntity).getHologram();
    }
    return null;
  }

  @Override
  public Hologram getPreHologram(int entityId) {
    return preHologram.get(entityId);
  }

  @Override
  public boolean isHologramEntity(org.bukkit.entity.Entity entity) {
    return getHologram(entity) != null;
  }

  private float clampYaw(float yaw) {
    return (yaw % 360 + 360) % 360;
  }

  private boolean addEntity(Entity entity) {
    try {
      return entity.world.addEntity(entity, SpawnReason.CUSTOM);
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  @Override
  public void sendActionBar(Player player, String message) {
    PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"), (byte) 2);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
  }

  @Override
  public void sendTitle(Player player, String title, String subtitle) {
    this.sendTitle(player, title, subtitle, 20, 60, 20);
  }

  @Override
  public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    EntityPlayer ep = ((CraftPlayer) player).getHandle();
    ep.playerConnection.sendPacket(new PacketPlayOutTitle(fadeIn, stay, fadeOut));
    ep.playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\": \"" + title + "\"}")));
    ep.playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\": \"" + subtitle + "\"}")));
  }

  @Override
  public void sendTabHeaderFooter(Player player, String header, String footer) {
    EntityPlayer ep = ((CraftPlayer) player).getHandle();

    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(ChatSerializer.a("{\"text\": \"" + header + "\"}"));
    Accessors.getField(packet.getClass(), "b").set(packet, ChatSerializer.a("{\"text\": \"" + footer + "\"}"));

    ep.playerConnection.sendPacket(packet);
  }

  @Override
  public void refreshPlayer(Player player) {
    EntityPlayer ep = ((CraftPlayer) player).getHandle();

    int entId = ep.getId();
    Location l = player.getLocation();

    PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
    PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entId);
    PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);
    PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
    PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
    PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
    PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
    PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
    PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
    PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(player.getInventory().getHeldItemSlot());

    for (Player players : Bukkit.getOnlinePlayers()) {
      if (players instanceof NPCHolder) {
        continue;
      }

      EntityPlayer epOn = ((CraftPlayer) players).getHandle();
      PlayerConnection con = epOn.playerConnection;
      if (players.equals(player)) {
        con.sendPacket(removeInfo);

        final boolean allow = player.getAllowFlight();
        final boolean flying = player.isFlying();
        final Location location = player.getLocation();
        final int level = player.getLevel();
        final float xp = player.getExp();
        final double maxHealth = player.getMaxHealth();
        final double health = player.getHealth();

        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
          con.sendPacket(new PacketPlayOutRespawn(players.getWorld().getEnvironment().getId(), epOn.getWorld().getDifficulty(), epOn.getWorld().getWorldData().getType(),
              epOn.playerInteractManager.getGameMode()));

          player.setAllowFlight(allow);
          if (flying) {
            player.setFlying(allow);
          }
          player.teleport(location);
          player.updateInventory();
          player.setLevel(level);
          player.setExp(xp);
          player.setMaxHealth(maxHealth);
          player.setHealth(health);
          epOn.updateAbilities();

          con.sendPacket(addInfo);
        }, 1L);
      } else {
        if (players.canSee(player) && players.getWorld().equals(player.getWorld())) {
          con.sendPacket(removeEntity);
          con.sendPacket(removeInfo);
          con.sendPacket(addInfo);
          con.sendPacket(addNamed);
          con.sendPacket(itemhand);
          con.sendPacket(helmet);
          con.sendPacket(chestplate);
          con.sendPacket(leggings);
          con.sendPacket(boots);
        } else if (players.canSee(player)) {
          con.sendPacket(removeInfo);
          con.sendPacket(addInfo);
        }
      }
    }
  }
}

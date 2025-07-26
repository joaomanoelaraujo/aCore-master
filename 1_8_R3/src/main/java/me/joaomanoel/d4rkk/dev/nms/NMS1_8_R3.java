package me.joaomanoel.d4rkk.dev.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.joaomanoel.d4rkk.dev.nms.enderdragon.MountableEnderDragon;
import me.joaomanoel.d4rkk.dev.nms.hologram.HologramEntity;
import me.joaomanoel.d4rkk.dev.nms.hologram.Hologram_8_R3;
import me.joaomanoel.d4rkk.dev.nms.npc.NPC_8_R3;
import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class NMS1_8_R3 implements NMS_Interface {

    private JavaPlugin plugin;

    @Override
    public void setupListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendActionBar(String text, Player player) {
        ProtocolManager protocolManager = NMSManager.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.CHAT);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(text));
        packet.getBytes().write(0, (byte) 2);
        protocolManager.sendServerPacket(player, packet);
    }

    @Override
    public void sendTablist(String header, String footer, Player player) {
        ProtocolManager protocolManager = NMSManager.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(header));
        packet.getChatComponents().write(1, WrappedChatComponent.fromText(footer));
        protocolManager.sendServerPacket(player, packet);
    }

    @Override
    public void createMountableEnderDragon(Player player) {
        MountableEnderDragon dragon = new MountableEnderDragon(player);
        addToWorld(player.getWorld(), dragon.getBukkitEntity(), CreatureSpawnEvent.SpawnReason.CUSTOM);
        dragon.getBukkitEntity().setPassenger(player);
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        ProtocolManager protocolManager = NMSManager.getProtocolManager();
        PacketContainer timesPacket = protocolManager.createPacket(PacketType.Play.Server.TITLE);
        timesPacket.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
        timesPacket.getIntegers().write(0, fadeIn).write(1, stay).write(2, fadeOut);
        protocolManager.sendServerPacket(player, timesPacket);

        PacketContainer titlePacket = protocolManager.createPacket(PacketType.Play.Server.TITLE);
        titlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
        titlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(title));
        protocolManager.sendServerPacket(player, titlePacket);

        PacketContainer subtitlePacket = protocolManager.createPacket(PacketType.Play.Server.TITLE);
        subtitlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
        subtitlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(subtitle));
        protocolManager.sendServerPacket(player, subtitlePacket);
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 20, 60, 20);
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
    public void playChestAction(Location location, boolean open) {
        ProtocolManager manager = NMSManager.getProtocolManager();
        PacketContainer container = manager.createPacket(PacketType.Play.Server.BLOCK_ACTION);
        container.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));
        container.getIntegers().write(0, open ? 1 : 0);
        container.getIntegers().write(1, 1);
        container.getBlocks().write(0, Material.CHEST);
        for (Player online : Bukkit.getOnlinePlayers()) {
            manager.sendServerPacket(online, container);
        }
    }

    @Override
    public void sendTabListAdd(Player player, Player listPlayer) {
        ProtocolManager manager = NMSManager.getProtocolManager();
        PacketContainer container = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(listPlayer);
        PlayerInfoData infoData = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(listPlayer.getName()));
        container.getPlayerInfoDataLists().write(0, Collections.singletonList(infoData));
        manager.sendServerPacket(player, container);
    }

    @Override
    public void sendTabListRemove(Player player, Player listPlayer) {
        ProtocolManager manager = NMSManager.getProtocolManager();
        PacketContainer container = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        container.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(listPlayer);
        PlayerInfoData infoData = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(listPlayer.getName()));
        container.getPlayerInfoDataLists().write(0, Collections.singletonList(infoData));
        manager.sendServerPacket(player, container);
    }

    @Override
    public void clearPathfinderGoal(Object entity) {
        if (entity instanceof Entity) {
            entity = ((CraftEntity) entity).getHandle();
        }

        net.minecraft.server.v1_8_R3.Entity handle = (net.minecraft.server.v1_8_R3.Entity) entity;
        if (handle instanceof EntityInsentient) {
            try {
                EntityInsentient entityInsentient = (EntityInsentient) handle;
                PathfinderGoalSelector goalSelector = entityInsentient.goalSelector;
                Class<?> clazz = goalSelector.getClass();
                Field field = clazz.getDeclaredField("b");
                field.setAccessible(true);
                List<?> list = (List<?>) field.get(goalSelector);
                list.clear();

                goalSelector = entityInsentient.targetSelector;
                clazz = goalSelector.getClass();
                field = clazz.getDeclaredField("c");
                field.setAccessible(true);
                list = (List<?>) field.get(goalSelector);
                list.clear();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void addToWorld(World world, Object entity, CreatureSpawnEvent.SpawnReason reason) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.spawnIn(((CraftWorld) world).getHandle());
        ((CraftWorld) world).getHandle().addEntity(nmsEntity, reason);
    }

    @Override
    public void removeToWorld(Object entity) {
        ((CraftEntity) entity).remove();
    }

    @Override
    public void look(Object entity, float yaw, float pitch) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity != null) {
            yaw = NMSManager.clampYaw(yaw);
            nmsEntity.yaw = yaw;
            setHeadYaw(entity, yaw);
            nmsEntity.pitch = pitch;
        }
    }
    @Override
    public void resendSkin(Player target, Player npc) {
        EntityPlayer ep = ((CraftPlayer) npc).getHandle();

        PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(ep);
        PacketPlayOutEntityHeadRotation head = new PacketPlayOutEntityHeadRotation(ep, (byte) (ep.yaw * 256F / 360F));

        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(remove);
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(add);
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(spawn);
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(head);
    }
    @Override
    public void setHeadYaw(Object entity, float yaw) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) nmsEntity;
            yaw = NMSManager.clampYaw(yaw);
            living.aK = yaw;
            if (living instanceof EntityHuman) {
                living.aI = yaw;
            }

            living.aL = yaw;
        }
    }

    @Override
    public void refreshPlayer(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        int entId = ep.getId();
        PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(entId);
        PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);
        PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
        PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(player.getInventory().getHeldItemSlot());

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players instanceof NpcEntity) {
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

                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    con.sendPacket(new PacketPlayOutRespawn(players.getWorld().getEnvironment().getId(), epOn.getWorld().getDifficulty(), epOn.getWorld().getWorldData().getType(), epOn.playerInteractManager.getGameMode()));
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

    @Override
    public HologramEntity createHologram(Location location) {
        Hologram_8_R3 entity = new Hologram_8_R3(location);
        addToWorld(location.getWorld(), entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        clearPathfinderGoal(entity);
        return entity;
    }

    @Override
    public NpcEntity createNPC(Location location, String name, String value, String signature) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        profile.getProperties().put("textures", new Property("textures", value, signature));
        NPC_8_R3 entity = new NPC_8_R3(location, this, profile);
        entity.setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
        entity.playerConnection = new PlayerConnection(entity.server, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), entity);
        return entity;
    }
}

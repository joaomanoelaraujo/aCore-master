package me.joaomanoel.d4rkk.dev.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.joaomanoel.d4rkk.dev.nms.hologram.HologramEntity;
import me.joaomanoel.d4rkk.dev.nms.hologram.Hologram_20_R2;
import me.joaomanoel.d4rkk.dev.nms.npc.NPC_20_R2;
import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class NMS1_20_R2 implements NMS_Interface {

    private JavaPlugin plugin;

    @Override
    public void setupListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendActionBar(String text, Player player) {
        ProtocolManager protocolManager = NMSManager.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SYSTEM_CHAT);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(text));
        packet.getBooleans().write(0, true);
        protocolManager.sendServerPacket(player, packet);
    }

    @Override
    public void sendTablist(String header, String footer, Player player) {
        player.setPlayerListHeaderFooter(header, footer);
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
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
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer");
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket");

            Method createPlayerAddPacket = packetClass.getMethod("createPlayerInitializing", Collection.class);

            Method getHandle = craftPlayerClass.getMethod("getHandle");
            Object targetEntityPlayer = getHandle.invoke(craftPlayerClass.cast(listPlayer));

            Object packet = createPlayerAddPacket.invoke(null, Collections.singletonList(targetEntityPlayer));

            Object receiverHandle = getHandle.invoke(craftPlayerClass.cast(player));
            Object connection = receiverHandle.getClass().getField("connection").get(receiverHandle);
            connection.getClass().getMethod("send", Class.forName("net.minecraft.network.protocol.Packet")).invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTabListRemove(Player player, Player listPlayer) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        packet.getUUIDLists().write(0, Collections.singletonList(listPlayer.getUniqueId()));
        manager.sendServerPacket(listPlayer, packet);
    }

    @Override
    public void clearPathfinderGoal(Object entity) {
        ((LivingEntity) entity).setAI(false);
    }

    @Override
    public void addToWorld(World world, Object entity, CreatureSpawnEvent.SpawnReason reason) {
        CraftEntity nmsEntity = (CraftEntity) entity;
        ((CraftWorld) world).getHandle().addFreshEntity(nmsEntity.getHandle(), reason);
    }

    @Override
    public void removeToWorld(Object entity) {
        ((CraftEntity) entity).remove();
    }

    @Override
    public void look(Object entity, float yaw, float pitch) {
        if (entity instanceof CraftEntity) {
            try {
                yaw = NMSManager.clampYaw(yaw);
                ((CraftEntity) entity).setRotation(yaw, pitch);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setHeadYaw(Object entity, float yaw) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity instanceof LivingEntity) {
            yaw = NMSManager.clampYaw(yaw);
            ((CraftEntity) entity).setRotation(yaw, ((LivingEntity) nmsEntity).getEyeLocation().getPitch());
        }
    }

    @Override
    public void refreshPlayer(Player player) {
        Location loc = player.getLocation();
        int level = player.getLevel();
        float xp = player.getExp();
        double health = player.getHealth();
        double maxHealth = player.getMaxHealth();
        boolean allowFlight = player.getAllowFlight();
        boolean isFlying = player.isFlying();

        for (Player other : Bukkit.getOnlinePlayers()) {
            other.hidePlayer(plugin, player);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player other : Bukkit.getOnlinePlayers()) {
                other.showPlayer(plugin, player);
            }

            player.teleport(loc);
            player.setLevel(level);
            player.setExp(xp);
            player.setMaxHealth(maxHealth);
            player.setHealth(health);
            player.setAllowFlight(allowFlight);
            if (isFlying) player.setFlying(true);
            player.updateInventory();
        }, 10L);
    }

    @Override
    public HologramEntity createHologram(Location location) {
        Hologram_20_R2 entity = new Hologram_20_R2(location);
        addToWorld(location.getWorld(), entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return entity;
    }

    @Override
    public NpcEntity createNPC(Location location, String name, String value, String signature) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        profile.getProperties().clear();
        profile.getProperties().put("textures", new Property("textures", value, signature));
        NPC_20_R2 entity = new NPC_20_R2(this, location, profile);
        entity.c = new PlayerConnection(MinecraftServer.getServer(), new NetworkManager(EnumProtocolDirection.b), entity, new CommonListenerCookie(profile, 0, ClientInformation.a(), false));
        entity.setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
        return entity;
    }
}

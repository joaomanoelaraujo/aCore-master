package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.replay.INPC;
import me.joaomanoel.d4rkk.dev.replay.MathUtils;
import me.joaomanoel.d4rkk.dev.replay.NPCManager;
import me.joaomanoel.d4rkk.dev.replay.StringUtils;
import me.joaomanoel.d4rkk.dev.replay.VersionUtil;
import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.*;

import java.lang.reflect.Method;
import java.util.*;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PacketNPC implements INPC {
    private int id;
    private UUID uuid;
    private String name;
    private int tabMode;
    private WrappedDataWatcher data;
    private WrappedGameProfile profile;
    private Location location;
    private Location origin;
    private float yaw;
    private float pitch;
    private Player[] visible;
    private Player oldVisible;
    private List<WrapperPlayServerEntityEquipment> lastEquipment;
    private boolean spawned = false;
    private boolean destroyed = false;

    public PacketNPC(int id, UUID uuid, String name) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.tabMode = 1;
        this.lastEquipment = new ArrayList<>();
    }

    public PacketNPC() {
        this(MathUtils.randInt(50000, 400000), UUID.randomUUID(), StringUtils.getRandomString(6));
    }

    @Override
    public void spawn(Location loc, int tabMode, Player... players) {
        if (spawned && !destroyed) {
            respawn(players);
            return;
        }

        this.tabMode = tabMode;
        this.visible = players;
        this.oldVisible = players.length > 0 ? players[0] : null;
        this.location = loc.clone();
        this.origin = loc.clone();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();
        this.spawned = true;
        this.destroyed = false;

        NPCManager.names.add(this.name);

        try {
            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_19)) {
                spawnModern(loc, tabMode, players);
            } else {
                spawnLegacy(loc, tabMode, players);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void spawnModern(Location loc, int tabMode, Player[] players) throws Exception {
        Class<?> wrapperClass = Class.forName("com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity");
        Object packet = wrapperClass.getConstructor().newInstance();

        setMethod(packet, "setEntityID", int.class, this.id);
        setMethod(packet, "setUniqueId", UUID.class, this.uuid);
        setMethod(packet, "setType", EntityType.class, EntityType.PLAYER);
        setMethod(packet, "setX", double.class, loc.getX());
        setMethod(packet, "setY", double.class, loc.getY());
        setMethod(packet, "setZ", double.class, loc.getZ());
        setMethod(packet, "setPitch", float.class, this.pitch);
        setMethod(packet, "setYaw", float.class, this.yaw);

        if (this.data != null) {
            setMethod(packet, "setMetadata", WrappedDataWatcher.class, this.data);
        }

        for (Player player : players) {
            if (player == null || !player.isOnline()) continue;

            if (tabMode != 0) {
                this.getInfoAddPacket().sendPacket(player);
            }
            invokeMethod(packet, "sendPacket", Player.class, player);

            // Enviar equipamento após spawn
            if (!lastEquipment.isEmpty()) {
                for (WrapperPlayServerEntityEquipment equipment : lastEquipment) {
                    equipment.sendPacket(player);
                }
            }

            if (tabMode == 1) {
                this.getInfoRemovePacket().sendPacket(player);
            }
        }
    }

    private void spawnLegacy(Location loc, int tabMode, Player[] players) throws Exception {
        Class<?> wrapperClass = Class.forName("com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn");
        Object packet = wrapperClass.getConstructor().newInstance();

        setMethod(packet, "setEntityID", int.class, this.id);
        setMethod(packet, "setPlayerUUID", UUID.class, this.uuid);
        setMethod(packet, "setPosition", org.bukkit.util.Vector.class, loc.toVector());
        setMethod(packet, "setYaw", float.class, this.yaw);
        setMethod(packet, "setPitch", float.class, this.pitch);

        if (this.data != null) {
            setMethod(packet, "setMetadata", WrappedDataWatcher.class, this.data);
        }

        for (Player player : players) {
            if (player == null || !player.isOnline()) continue;

            if (tabMode != 0) {
                this.getInfoAddPacket().sendPacket(player);
            }
            invokeMethod(packet, "sendPacket", Player.class, player);

            // Enviar equipamento após spawn
            if (!lastEquipment.isEmpty()) {
                for (WrapperPlayServerEntityEquipment equipment : lastEquipment) {
                    equipment.sendPacket(player);
                }
            }

            if (tabMode == 1) {
                this.getInfoRemovePacket().sendPacket(player);
            }
        }
    }

    @Override
    public void respawn(Player... players) {
        if (destroyed) {
            spawned = false;
            spawn(location, tabMode, players);
            return;
        }

        this.visible = players;
        try {
            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_19)) {
                respawnModern(players);
            } else {
                respawnLegacy(players);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void respawnModern(Player[] players) throws Exception {
        Class<?> wrapperClass = Class.forName("com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity");
        Object packet = wrapperClass.getConstructor().newInstance();

        setMethod(packet, "setEntityID", int.class, this.id);
        setMethod(packet, "setUniqueId", UUID.class, this.uuid);
        setMethod(packet, "setType", EntityType.class, EntityType.PLAYER);
        setMethod(packet, "setX", double.class, this.location.getX());
        setMethod(packet, "setY", double.class, this.location.getY());
        setMethod(packet, "setZ", double.class, this.location.getZ());
        setMethod(packet, "setPitch", float.class, this.pitch);
        setMethod(packet, "setYaw", float.class, this.yaw);

        if (this.data != null) {
            setMethod(packet, "setMetadata", WrappedDataWatcher.class, this.data);
        }

        for (Player player : players) {
            if (player == null || !player.isOnline()) continue;

            // Adicionar ao tab se necessário
            if (tabMode != 0) {
                this.getInfoAddPacket().sendPacket(player);
            }

            invokeMethod(packet, "sendPacket", Player.class, player);

            for (WrapperPlayServerEntityEquipment equipment : this.lastEquipment) {
                equipment.sendPacket(player);
            }

            // Remover do tab se necessário
            if (tabMode == 1) {
                this.getInfoRemovePacket().sendPacket(player);
            }
        }

        destroyed = false;
        spawned = true;
    }

    private void respawnLegacy(Player[] players) throws Exception {
        Class<?> wrapperClass = Class.forName("com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn");
        Object packet = wrapperClass.getConstructor().newInstance();

        setMethod(packet, "setEntityID", int.class, this.id);
        setMethod(packet, "setPlayerUUID", UUID.class, this.uuid);
        setMethod(packet, "setPosition", org.bukkit.util.Vector.class, this.location.toVector());
        setMethod(packet, "setYaw", float.class, this.yaw);
        setMethod(packet, "setPitch", float.class, this.pitch);

        if (this.data != null) {
            setMethod(packet, "setMetadata", WrappedDataWatcher.class, this.data);
        }

        for (Player player : players) {
            if (player == null || !player.isOnline()) continue;

            // Adicionar ao tab se necessário
            if (tabMode != 0) {
                this.getInfoAddPacket().sendPacket(player);
            }

            invokeMethod(packet, "sendPacket", Player.class, player);

            for (WrapperPlayServerEntityEquipment equipment : this.lastEquipment) {
                equipment.sendPacket(player);
            }

            // Remover do tab se necessário
            if (tabMode == 1) {
                this.getInfoRemovePacket().sendPacket(player);
            }
        }

        destroyed = false;
        spawned = true;
    }

    private void setMethod(Object obj, String methodName, Class<?> paramType, Object value) throws Exception {
        Method method = obj.getClass().getMethod(methodName, paramType);
        method.invoke(obj, value);
    }

    private void invokeMethod(Object obj, String methodName, Class<?> paramType, Object value) throws Exception {
        Method method = obj.getClass().getMethod(methodName, paramType);
        method.invoke(obj, value);
    }

    @Override
    public void despawn() {
        if (destroyed) return;

        WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
            destroyPacket.getHandle().getIntLists().write(0, Collections.singletonList(this.id));
        } else {
            destroyPacket.setEntityIds(new int[]{this.id});
        }

        for (Player player : this.visible) {
            if (player == null || !player.isOnline()) continue;
            destroyPacket.sendPacket(player);
        }

        destroyed = true;
        spawned = false;
    }

    @Override
    public void remove() {
        NPCManager.names.remove(this.name);

        WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
            destroyPacket.getHandle().getIntLists().write(0, Collections.singletonList(this.id));
        } else {
            destroyPacket.setEntityIds(new int[]{this.id});
        }

        if (this.oldVisible != null && this.oldVisible.isOnline()) {
            if (this.tabMode == 2) {
                this.getInfoRemovePacket().sendPacket(this.oldVisible);
            }
            destroyPacket.sendPacket(this.oldVisible);
        }

        destroyed = true;
        spawned = false;
        visible = null;
    }

    @Override
    public void teleport(Location loc, boolean onGround) {
        if (destroyed || !spawned) return;

        this.location = loc.clone();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();

        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();
        packet.setEntityID(this.id);
        packet.setX(loc.getX());
        packet.setY(loc.getY());
        packet.setZ(loc.getZ());
        packet.setPitch(loc.getPitch());
        packet.setYaw(loc.getYaw());
        packet.setOnGround(onGround);

        for (Player player : this.visible) {
            if (player == null || !player.isOnline()) continue;
            packet.sendPacket(player);
        }
    }

    @Override
    public void move(Location loc, boolean onGround, float yaw, float pitch) {
        if (destroyed || !spawned) return;

        WrapperPlayServerRelEntityMoveLook packet = new WrapperPlayServerRelEntityMoveLook();
        WrapperPlayServerEntityHeadRotation head = new WrapperPlayServerEntityHeadRotation();

        packet.setEntityID(this.id);
        head.setEntityID(this.id);
        head.setHeadYaw((byte)(yaw * 256.0f / 360.0f));

        packet.setDx((short)((loc.getX() * 32.0 - this.location.getX() * 32.0) * 128.0));
        packet.setDy((short)((loc.getY() * 32.0 - this.location.getY() * 32.0) * 128.0));
        packet.setDz((short)((loc.getZ() * 32.0 - this.location.getZ() * 32.0) * 128.0));
        packet.setPitch(pitch);
        packet.setYaw(yaw);

        this.location = loc.clone();
        this.yaw = yaw;
        this.pitch = pitch;

        for (Player player : this.visible) {
            if (player == null || !player.isOnline()) continue;
            packet.sendPacket(player);
            head.sendPacket(player);
        }
    }

    @Override
    public void look(float yaw, float pitch) {
        if (destroyed || !spawned) return;

        this.yaw = yaw;
        this.pitch = pitch;

        WrapperPlayServerEntityLook lookPacket = new WrapperPlayServerEntityLook();
        WrapperPlayServerEntityHeadRotation head = new WrapperPlayServerEntityHeadRotation();

        head.setEntityID(this.id);
        head.setHeadYaw((byte)(yaw * 256.0f / 360.0f));
        lookPacket.setEntityID(this.id);
        lookPacket.setOnGround(true);
        lookPacket.setPitch(pitch);
        lookPacket.setYaw(yaw);

        for (Player player : this.visible) {
            if (player == null || !player.isOnline()) continue;
            lookPacket.sendPacket(player);
            head.sendPacket(player);
        }
    }

    @Override
    public void updateMetadata() {
        if (destroyed || !spawned) return;

        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
        packet.setEntityID(this.id);
        packet.setMetadata(this.data.getWatchableObjects());

        for (Player player : this.visible) {
            if (player == null || !player.isOnline()) continue;
            packet.sendPacket(player);
        }
    }

    @Override
    public void animate(int id) {
        if (destroyed || !spawned) return;

        WrapperPlayServerAnimation packet = new WrapperPlayServerAnimation();
        packet.setEntityID(this.id);
        packet.setAnimation(id);

        for (Player player : this.visible) {
            if (player == null || !player.isOnline()) continue;
            packet.sendPacket(player);
        }
    }

    @Override
    public void sleep(Location loc) {
        if (destroyed || !spawned) return;

        WrapperPlayServerBed packet = new WrapperPlayServerBed();
        packet.setEntityID(this.id);
        packet.setLocation(new BlockPosition(loc.toVector()));

        for (Player player : this.visible) {
            if (player == null || !player.isOnline()) continue;
            packet.sendPacket(player);
        }
    }

    @Override
    public void addToTeam(String team) {
        if (destroyed || !spawned) return;

        try {
            Class<?> wrapperClass = Class.forName("com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam");
            Object packet = wrapperClass.getConstructor().newInstance();
            setMethod(packet, "setName", String.class, team);
            setMethod(packet, "setMode", int.class, 3);
            setMethod(packet, "setPlayers", java.util.List.class, Collections.singletonList(this.name));

            for (Player player : this.visible) {
                if (player == null || !player.isOnline()) continue;
                invokeMethod(packet, "sendPacket", Player.class, player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WrapperPlayServerPlayerInfo getInfoAddPacket() {
        WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo();
        infoPacket.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        WrappedGameProfile profile = this.profile != null ? this.profile : new WrappedGameProfile(this.uuid, this.name);
        PlayerInfoData data = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(this.name));
        ArrayList<PlayerInfoData> dataList = new ArrayList<>();
        dataList.add(data);
        infoPacket.setData(dataList);
        return infoPacket;
    }

    private WrapperPlayServerPlayerInfo getInfoRemovePacket() {
        WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo();
        infoPacket.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        WrappedGameProfile profile = this.profile != null ? this.profile : new WrappedGameProfile(this.uuid, this.name);
        PlayerInfoData data = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(this.name));
        ArrayList<PlayerInfoData> dataList = new ArrayList<>();
        dataList.add(data);
        infoPacket.setData(dataList);
        return infoPacket;
    }

    // Getters e Setters
    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void setData(WrappedDataWatcher data) {
        this.data = data;
    }

    @Override
    public WrappedDataWatcher getData() {
        return this.data;
    }

    @Override
    public void setProfile(WrappedGameProfile profile) {
        this.profile = profile;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public Location getLocation() {
        return this.location != null ? this.location.clone() : null;
    }

    @Override
    public void setOrigin(Location origin) {
        this.origin = origin != null ? origin.clone() : null;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location != null ? location.clone() : null;
    }

    @Override
    public Location getOrigin() {
        return this.origin != null ? this.origin.clone() : null;
    }

    @Override
    public Player[] getVisible() {
        return this.visible;
    }

    @Override
    public void setLastEquipment(List<WrapperPlayServerEntityEquipment> list) {
        this.lastEquipment = new ArrayList<>(list);
    }

    public boolean isSpawned() {
        return spawned;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
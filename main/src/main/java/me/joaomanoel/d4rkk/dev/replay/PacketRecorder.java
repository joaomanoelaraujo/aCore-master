/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerAction
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerDigType
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.packetwrapper.WrapperPlayClientBlockDig;
import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.packetwrapper.WrapperPlayClientLook;
import com.comphenix.packetwrapper.WrapperPlayClientPosition;
import com.comphenix.packetwrapper.WrapperPlayClientPositionLook;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMove;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMoveLook;
import com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class PacketRecorder
extends AbstractListener {
    private static final List<PacketType> RECORDED_PACKETS = new ArrayList<PacketType>(Arrays.asList(PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK, PacketType.Play.Client.ENTITY_ACTION, PacketType.Play.Client.ARM_ANIMATION, PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.ENTITY_DESTROY, PacketType.Play.Server.ENTITY_VELOCITY, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_LOOK, PacketType.Play.Server.POSITION, PacketType.Play.Server.ENTITY_TELEPORT));
    private PacketAdapter packetAdapter;
    private final HashMap<String, List<PacketData>> packetData = new HashMap<>();
    private final List<Integer> spawnedItems = new ArrayList<>();
    private final HashMap<Integer, EntityData> spawnedEntities = new HashMap<>();
    private final HashMap<Integer, String> entityLookup = new HashMap<>();
    private final HashMap<Integer, Entity> idLookup = new HashMap<>();
    private final List<Integer> spawnedHooks = new ArrayList<>();
    private final Recorder recorder;
    private final ReplayOptimizer optimizer;
    private AbstractListener compListener;
    private AbstractListener listener;

    public PacketRecorder(Recorder recorder) {
        this.recorder = recorder;
        this.optimizer = new ReplayOptimizer();
    }

    @Override
    public void register() {
        super.register();
        if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_18)) {
            RECORDED_PACKETS.add(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        }
        this.packetAdapter = new PacketAdapter(Core.getInstance(), ListenerPriority.HIGHEST, RECORDED_PACKETS){

            public void onPacketReceiving(PacketEvent event) {
                if (event.getPlayer() != null && PacketRecorder.this.recorder.getPlayers().contains(event.getPlayer().getName())) {
                    AbstractPacket packet;
                    Player p = event.getPlayer();
                    PacketData data = null;
                    if (event.getPacketType() == PacketType.Play.Client.POSITION) {
                        packet = new WrapperPlayClientPosition(event.getPacket());
                        data = new MovingData(((WrapperPlayClientPosition)packet).getX(), ((WrapperPlayClientPosition)packet).getY(), ((WrapperPlayClientPosition)packet).getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
                        if (PacketRecorder.this.recorder.getData().getWatcher(p.getName()).isBurning() && p.getFireTicks() <= 20) {
                            PacketRecorder.this.recorder.getData().getWatcher(p.getName()).setBurning(false);
                            PacketRecorder.this.addData(p.getName(), new MetadataUpdate(false, PacketRecorder.this.recorder.getData().getWatcher(p.getName()).isBlocking(), PacketRecorder.this.recorder.getData().getWatcher(p.getName()).isElytra()));
                        }
                    }
                    if (event.getPacketType() == PacketType.Play.Client.LOOK) {
                        packet = new WrapperPlayClientLook(event.getPacket());
                        data = new MovingData(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), ((WrapperPlayClientLook)packet).getPitch(), ((WrapperPlayClientLook)packet).getYaw());
                    }
                    if (event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
                        packet = new WrapperPlayClientPositionLook(event.getPacket());
                        data = new MovingData(((WrapperPlayClientPositionLook)packet).getX(), ((WrapperPlayClientPositionLook)packet).getY(), ((WrapperPlayClientPositionLook)packet).getZ(), ((WrapperPlayClientPositionLook)packet).getPitch(), ((WrapperPlayClientPositionLook)packet).getYaw());
                    }
                    if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION && (((WrapperPlayClientEntityAction)(packet = new WrapperPlayClientEntityAction(event.getPacket()))).getAction() == EnumWrappers.PlayerAction.START_SNEAKING || ((WrapperPlayClientEntityAction)packet).getAction() == EnumWrappers.PlayerAction.STOP_SNEAKING)) {
                        data = new EntityActionData(((WrapperPlayClientEntityAction)packet).getAction());
                    }
                    if (event.getPacketType() == PacketType.Play.Client.ARM_ANIMATION) {
                        data = new AnimationData(0);
                    }
                    if (event.getPacketType() == PacketType.Play.Client.BLOCK_DIG && ((WrapperPlayClientBlockDig)(packet = new WrapperPlayClientBlockDig(event.getPacket()))).getStatus() == EnumWrappers.PlayerDigType.RELEASE_USE_ITEM && PacketRecorder.this.recorder.getData().getWatcher(p.getName()).isBlocking()) {
                        PlayerWatcher watcher = PacketRecorder.this.recorder.getData().getWatcher(p.getName());
                        watcher.setBlocking(false);
                        PacketRecorder.this.addData(p.getName(), MetadataUpdate.fromWatcher(watcher));
                    }
                    PacketRecorder.this.addData(event.getPlayer().getName(), data);
                }
            }

            public void onPacketSending(PacketEvent event) {
                Location loc;
                Player p = event.getPlayer();
                if (!PacketRecorder.this.recorder.getPlayers().contains(p.getName())) {
                    return;
                }

                // SPAWN_ENTITY (genérico)
                if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
                    Entity en;
                    // usa sua wrapper local (me.joaomanoel.d4rkk.dev.replay.WrapperPlayServerSpawnEntity)
                    WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(event.getPacket());
                    WrapperPlayServerSpawnEntity oldPacket = new WrapperPlayServerSpawnEntity(event.getPacket());

                    int type = VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8) ? oldPacket.getType() : packet.getType();

                    LocationData location = VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)
                            ? new LocationData(oldPacket.getX(), oldPacket.getY(), oldPacket.getZ(), p.getWorld().getName())
                            : new LocationData(packet.getX(), packet.getY(), packet.getZ(), p.getWorld().getName());

                    // DROPPED_ITEM spawn
                    if ((type == 2 || (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14) &&
                            event.getPacket().getEntityTypeModifier().read(0) == EntityType.DROPPED_ITEM))
                            && !PacketRecorder.this.spawnedItems.contains(packet.getEntityID())
                            && (en = packet.getEntity(p.getWorld())) != null && en instanceof Item) {

                        Item item = (Item) en;
                        LocationData velocity = LocationData.fromLocation(item.getVelocity().toLocation(p.getWorld()));
                        PacketRecorder.this.addData(p.getName(), new EntityItemData(0, packet.getEntityID(), NPCManager.fromItemStack(item.getItemStack()), location, velocity));
                        PacketRecorder.this.spawnedItems.add(packet.getEntityID());
                    }

                    // FISHING_HOOK spawn
                    if ((type == 90 || (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14) &&
                            event.getPacket().getEntityTypeModifier().read(0) == EntityType.FISHING_HOOK))
                            && !PacketRecorder.this.spawnedHooks.contains(packet.getEntityID())) {

                        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                            PacketRecorder.this.addData(p.getName(), new FishingData(oldPacket.getEntityID(), location, oldPacket.getOptionalSpeedX(), oldPacket.getOptionalSpeedY(), oldPacket.getOptionalSpeedZ()));
                        } else {
                            PacketRecorder.this.addData(p.getName(), new FishingData(packet.getEntityID(), location, packet.getOptionalSpeedX(), packet.getOptionalSpeedY(), packet.getOptionalSpeedZ()));
                        }
                        PacketRecorder.this.spawnedHooks.add(packet.getEntityID());
                    }

                    // Entidades modernas (1.19+)
                    if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_19) && ConfigManager.RECORD_ENTITIES) {
                        EntityType livingType = event.getPacket().getEntityTypeModifier().read(0);
                        if (EntityMappings.getInstance().getTypeId(livingType.toString()) != 0) {
                            LocationData locationData = new LocationData(packet.getX(), packet.getY(), packet.getZ(), p.getWorld().getName());
                            if (!PacketRecorder.this.spawnedEntities.containsKey(packet.getEntityID())) {
                                EntityData entData = new EntityData(0, packet.getEntityID(), locationData, livingType.toString());
                                PacketRecorder.this.addData(p.getName(), entData);
                                PacketRecorder.this.spawnedEntities.put(packet.getEntityID(), entData);
                                PacketRecorder.this.entityLookup.put(packet.getEntityID(), p.getName());
                                PacketRecorder.this.idLookup.put(packet.getEntityID(), packet.getEntity(p.getWorld()));
                            }
                        }
                    }
                }

                if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING && ConfigManager.RECORD_ENTITIES) {
                    com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving packet =
                            new com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving(event.getPacket());

                    EntityType type = packet.getType();
                    if (type == null) {
                        Entity e = packet.getEntity(p.getWorld());
                        if (e != null) type = e.getType();
                    }

                    if (!PacketRecorder.this.spawnedEntities.containsKey(packet.getEntityID())) {
                        LocationData location = null;
                        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                            WrapperPlayServerSpawnEntityLiving oldPacket = new WrapperPlayServerSpawnEntityLiving(event.getPacket());
                            location = new LocationData(oldPacket.getX(), oldPacket.getY(), oldPacket.getZ(), p.getWorld().getName());
                        } else {
                            location = new LocationData(packet.getX(), packet.getY(), packet.getZ(), p.getWorld().getName());
                        }
                        EntityData entData = new EntityData(0, packet.getEntityID(), location, type.toString());
                        PacketRecorder.this.addData(p.getName(), entData);
                        PacketRecorder.this.spawnedEntities.put(packet.getEntityID(), entData);
                        PacketRecorder.this.entityLookup.put(packet.getEntityID(), p.getName());
                        PacketRecorder.this.idLookup.put(packet.getEntityID(), packet.getEntity(p.getWorld()));
                    }
                }

                // ENTITY_DESTROY
                if (event.getPacketType() == PacketType.Play.Server.ENTITY_DESTROY) {
                    WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy(event.getPacket());
                    List<Integer> entityIds;
                    if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
                        entityIds = packet.getHandle().getIntLists().read(0);
                    } else {
                        entityIds = IntStream.of(packet.getEntityIDs()).boxed().collect(Collectors.toList());
                    }

                    for (Integer id : entityIds) {
                        if (PacketRecorder.this.spawnedItems.contains(id)) {
                            PacketRecorder.this.addData(p.getName(), new EntityItemData(1, id, null, null, null));
                            PacketRecorder.this.spawnedItems.remove(id);
                        }

                        if (PacketRecorder.this.spawnedEntities.containsKey(id) &&
                                (PacketRecorder.this.idLookup.get(id) == null || (PacketRecorder.this.idLookup.get(id) != null && PacketRecorder.this.idLookup.get(id).isDead()))) {

                            PacketRecorder.this.addData(p.getName(), new EntityData(1, id, PacketRecorder.this.spawnedEntities.get(id).getLocation(), PacketRecorder.this.spawnedEntities.get(id).getType()));
                            PacketRecorder.this.spawnedEntities.remove(id);
                            PacketRecorder.this.entityLookup.remove(id);
                            PacketRecorder.this.idLookup.remove(id);
                        }

                        if (PacketRecorder.this.spawnedHooks.contains(id)) {
                            PacketRecorder.this.addData(p.getName(), new EntityItemData(2, id, null, null, null));
                            PacketRecorder.this.spawnedHooks.remove(id);
                        }
                    }
                }

                // ENTITY_VELOCITY
                if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
                    WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity(event.getPacket());
                    int entityId = packet.getEntityID();
                    if (PacketRecorder.this.spawnedHooks.contains(entityId) ||
                            (PacketRecorder.this.entityLookup.containsKey(entityId) && PacketRecorder.this.entityLookup.get(entityId).equalsIgnoreCase(p.getName()))) {
                        PacketRecorder.this.addData(p.getName(), new VelocityData(entityId, packet.getVelocityX(), packet.getVelocityY(), packet.getVelocityZ()));
                    }
                }

                // REL_ENTITY_MOVE
                if (event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE) {
                    WrapperPlayServerRelEntityMove packet = new WrapperPlayServerRelEntityMove(event.getPacket());
                    int entityId = packet.getEntityID();
                    if (PacketRecorder.this.entityLookup.containsKey(entityId) && PacketRecorder.this.entityLookup.get(entityId).equalsIgnoreCase(p.getName())) {
                        loc = PacketRecorder.this.checkEntityLocation(packet.getEntity(p.getWorld()));
                        if (loc != null) {
                            PacketRecorder.this.addData(p.getName(), new EntityMovingData(entityId, loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw()));
                        }
                    }
                }

                // REL_ENTITY_MOVE_LOOK
                if (event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
                    WrapperPlayServerRelEntityMoveLook packet = new WrapperPlayServerRelEntityMoveLook(event.getPacket());
                    int entityId = packet.getEntityID();
                    if (PacketRecorder.this.entityLookup.containsKey(entityId) && PacketRecorder.this.entityLookup.get(entityId).equalsIgnoreCase(p.getName())) {
                        loc = PacketRecorder.this.checkEntityLocation(packet.getEntity(p.getWorld()));
                        if (loc != null) {
                            PacketRecorder.this.addData(p.getName(), new EntityMovingData(entityId, loc.getX(), loc.getY(), loc.getZ(), packet.getPitch(), packet.getYaw()));
                        }
                    }
                }

                // ENTITY_TELEPORT
                if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT) {
                    WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(event.getPacket());
                    int entityId = packet.getEntityID();
                    if (PacketRecorder.this.entityLookup.containsKey(entityId) && PacketRecorder.this.entityLookup.get(entityId).equalsIgnoreCase(p.getName())) {
                        loc = PacketRecorder.this.checkEntityLocation(packet.getEntity(p.getWorld()));
                        if (loc != null) {
                            PacketRecorder.this.addData(p.getName(), new EntityMovingData(entityId, loc.getX(), loc.getY(), loc.getZ(), packet.getPitch(), packet.getYaw()));
                        }
                    }
                }
            }

        };
        ProtocolLibrary.getProtocolManager().addPacketListener(this.packetAdapter);
        this.registerExternalListeners();
    }

    @Override
    public void unregister() {
        super.unregister();
        ProtocolLibrary.getProtocolManager().removePacketListener(this.packetAdapter);
        if (this.compListener != null) {
            this.compListener.unregister();
        }
        this.listener.unregister();
    }

    private void registerExternalListeners() {
        if (!VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            this.compListener = new CompListener(this);
            this.compListener.register();
        }
        this.listener = new RecordingListener(this);
        this.listener.register();
    }

    private Location checkEntityLocation(Entity en) {
        if (en == null) {
            return null;
        }
        return en.getLocation();
    }

    public void addData(String name, PacketData data) {
        if (!this.optimizer.shouldRecord(data)) {
            return;
        }
        List<PacketData> list = new ArrayList<PacketData>();
        if (this.packetData.containsKey(name)) {
            list = this.packetData.getOrDefault(name, new ArrayList());
        }
        list.add(data);
        this.packetData.put(name, list);
    }

    public HashMap<String, List<PacketData>> getPacketData() {
        return this.packetData;
    }

    public HashMap<Integer, String> getEntityLookup() {
        return this.entityLookup;
    }

    public Recorder getRecorder() {
        return this.recorder;
    }
}


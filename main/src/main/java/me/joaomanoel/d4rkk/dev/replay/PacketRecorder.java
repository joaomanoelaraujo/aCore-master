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
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketRecorder
extends AbstractListener {
    private static final List<PacketType> RECORDED_PACKETS = new ArrayList<PacketType>(Arrays.asList(PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK, PacketType.Play.Client.ENTITY_ACTION, PacketType.Play.Client.ARM_ANIMATION, PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.ENTITY_DESTROY, PacketType.Play.Server.ENTITY_VELOCITY, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_LOOK, PacketType.Play.Server.POSITION, PacketType.Play.Server.ENTITY_TELEPORT));
    private PacketAdapter packetAdapter;
    private HashMap<String, List<PacketData>> packetData = new HashMap();
    private List<Integer> spawnedItems = new ArrayList<Integer>();
    private HashMap<Integer, EntityData> spawnedEntities = new HashMap();
    private HashMap<Integer, String> entityLookup = new HashMap();
    private HashMap<Integer, Entity> idLookup = new HashMap();
    private List<Integer> spawnedHooks = new ArrayList<Integer>();
    private Recorder recorder;
    private ReplayOptimizer optimizer;
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
        this.packetAdapter = new PacketAdapter((Plugin)Core.getInstance(), ListenerPriority.HIGHEST, RECORDED_PACKETS){

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
                AbstractPacket packet;
                Player p = event.getPlayer();
                if (!PacketRecorder.this.recorder.getPlayers().contains(p.getName())) {
                    return;
                }
                if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
                    Entity en;
                    packet = new WrapperPlayServerSpawnEntity(event.getPacket());
                    com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntity oldPacket = new com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntity(event.getPacket());
                    int type = VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8) ? oldPacket.getType() : ((WrapperPlayServerSpawnEntity)packet).getType();
                    LocationData location = null;
                    location = VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8) ? new LocationData(oldPacket.getX(), oldPacket.getY(), oldPacket.getZ(), p.getWorld().getName()) : new LocationData(((WrapperPlayServerSpawnEntity)packet).getX(), ((WrapperPlayServerSpawnEntity)packet).getY(), ((WrapperPlayServerSpawnEntity)packet).getZ(), p.getWorld().getName());
                    if ((type == 2 || VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14) && event.getPacket().getEntityTypeModifier().read(0) == EntityType.DROPPED_ITEM) && !PacketRecorder.this.spawnedItems.contains(((WrapperPlayServerSpawnEntity)packet).getEntityID()) && (en = ((WrapperPlayServerSpawnEntity)packet).getEntity(p.getWorld())) != null && en instanceof Item) {
                        Item item = (Item)en;
                        LocationData velocity = LocationData.fromLocation(item.getVelocity().toLocation(p.getWorld()));
                        PacketRecorder.this.addData(p.getName(), new EntityItemData(0, ((WrapperPlayServerSpawnEntity)packet).getEntityID(), NPCManager.fromItemStack(item.getItemStack()), location, velocity));
                        PacketRecorder.this.spawnedItems.add(((WrapperPlayServerSpawnEntity)packet).getEntityID());
                    }
                    if ((type == 90 || VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14) && event.getPacket().getEntityTypeModifier().read(0) == EntityType.FISHING_HOOK) && !PacketRecorder.this.spawnedHooks.contains(((WrapperPlayServerSpawnEntity)packet).getEntityID())) {
                        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                            PacketRecorder.this.addData(p.getName(), new FishingData(oldPacket.getEntityID(), location, oldPacket.getOptionalSpeedX(), oldPacket.getOptionalSpeedY(), oldPacket.getOptionalSpeedZ()));
                        } else {
                            PacketRecorder.this.addData(p.getName(), new FishingData(((WrapperPlayServerSpawnEntity)packet).getEntityID(), location, ((WrapperPlayServerSpawnEntity)packet).getOptionalSpeedX(), ((WrapperPlayServerSpawnEntity)packet).getOptionalSpeedY(), ((WrapperPlayServerSpawnEntity)packet).getOptionalSpeedZ()));
                        }
                        PacketRecorder.this.spawnedHooks.add(((WrapperPlayServerSpawnEntity)packet).getEntityID());
                    }
                    if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_19) && ConfigManager.RECORD_ENTITIES) {
                        EntityType livingType = (EntityType)event.getPacket().getEntityTypeModifier().read(0);
                        if (EntityMappings.getInstance().getTypeId(livingType.toString()) != 0) {
                            LocationData locationData = new LocationData(((WrapperPlayServerSpawnEntity)packet).getX(), ((WrapperPlayServerSpawnEntity)packet).getY(), ((WrapperPlayServerSpawnEntity)packet).getZ(), p.getWorld().getName());
                            if (!PacketRecorder.this.spawnedEntities.containsKey(((WrapperPlayServerSpawnEntity)packet).getEntityID())) {
                                EntityData entData = new EntityData(0, ((WrapperPlayServerSpawnEntity)packet).getEntityID(), locationData, livingType.toString());
                                PacketRecorder.this.addData(p.getName(), entData);
                                PacketRecorder.this.spawnedEntities.put(((WrapperPlayServerSpawnEntity)packet).getEntityID(), entData);
                                PacketRecorder.this.entityLookup.put(((WrapperPlayServerSpawnEntity)packet).getEntityID(), p.getName());
                                PacketRecorder.this.idLookup.put(((WrapperPlayServerSpawnEntity)packet).getEntityID(), ((WrapperPlayServerSpawnEntity)packet).getEntity(p.getWorld()));
                            }
                        }
                    }
                }
                if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING && ConfigManager.RECORD_ENTITIES) {
                    packet = new com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving(event.getPacket());
                    EntityType type = ((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getType();
                    if (type == null) {
                        type = ((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getEntity(p.getWorld()).getType();
                    }
                    if (!PacketRecorder.this.spawnedEntities.containsKey(((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getEntityID())) {
                        LocationData location = null;
                        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                            WrapperPlayServerSpawnEntityLiving oldPacket = new WrapperPlayServerSpawnEntityLiving(event.getPacket());
                            location = new LocationData(oldPacket.getX(), oldPacket.getY(), oldPacket.getZ(), p.getWorld().getName());
                        } else {
                            location = new LocationData(((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getX(), ((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getY(), ((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getZ(), p.getWorld().getName());
                        }
                        EntityData entData = new EntityData(0, ((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getEntityID(), location, type.toString());
                        PacketRecorder.this.addData(p.getName(), entData);
                        PacketRecorder.this.spawnedEntities.put(((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getEntityID(), entData);
                        PacketRecorder.this.entityLookup.put(((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getEntityID(), p.getName());
                        PacketRecorder.this.idLookup.put(((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getEntityID(), ((com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving)packet).getEntity(p.getWorld()));
                    }
                }
                if (event.getPacketType() == PacketType.Play.Server.ENTITY_DESTROY) {
                    packet = new WrapperPlayServerEntityDestroy(event.getPacket());
                }
                if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
                    packet = new WrapperPlayServerEntityVelocity(event.getPacket());
                    if (PacketRecorder.this.spawnedHooks.contains(((WrapperPlayServerEntityVelocity)packet).getEntityID()) || PacketRecorder.this.entityLookup.containsKey(((WrapperPlayServerEntityVelocity)packet).getEntityID()) && ((String)PacketRecorder.this.entityLookup.get(((WrapperPlayServerEntityVelocity)packet).getEntityID())).equalsIgnoreCase(p.getName())) {
                        PacketRecorder.this.addData(p.getName(), new VelocityData(((WrapperPlayServerEntityVelocity)packet).getEntityID(), ((WrapperPlayServerEntityVelocity)packet).getVelocityX(), ((WrapperPlayServerEntityVelocity)packet).getVelocityY(), ((WrapperPlayServerEntityVelocity)packet).getVelocityZ()));
                    }
                }
                if (event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE) {
                    packet = new WrapperPlayServerRelEntityMove(event.getPacket());
                    if (PacketRecorder.this.entityLookup.containsKey(((WrapperPlayServerRelEntityMove)packet).getEntityID()) && ((String)PacketRecorder.this.entityLookup.get(((WrapperPlayServerRelEntityMove)packet).getEntityID())).equalsIgnoreCase(p.getName()) && (loc = PacketRecorder.this.checkEntityLocation(((WrapperPlayServerRelEntityMove)packet).getEntity(p.getWorld()))) != null) {
                        PacketRecorder.this.addData(p.getName(), new EntityMovingData(((WrapperPlayServerRelEntityMove)packet).getEntityID(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw()));
                    }
                }
                if (event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
                    packet = new WrapperPlayServerRelEntityMoveLook(event.getPacket());
                    if (PacketRecorder.this.entityLookup.containsKey(((WrapperPlayServerRelEntityMoveLook)packet).getEntityID()) && ((String)PacketRecorder.this.entityLookup.get(((WrapperPlayServerRelEntityMoveLook)packet).getEntityID())).equalsIgnoreCase(p.getName()) && (loc = PacketRecorder.this.checkEntityLocation(((WrapperPlayServerRelEntityMoveLook)packet).getEntity(p.getWorld()))) != null) {
                        PacketRecorder.this.addData(p.getName(), new EntityMovingData(((WrapperPlayServerRelEntityMoveLook)packet).getEntityID(), loc.getX(), loc.getY(), loc.getZ(), ((WrapperPlayServerRelEntityMoveLook)packet).getPitch(), ((WrapperPlayServerRelEntityMoveLook)packet).getYaw()));
                    }
                }
                if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT) {
                    packet = new WrapperPlayServerEntityTeleport(event.getPacket());
                    if (PacketRecorder.this.entityLookup.containsKey(((WrapperPlayServerEntityTeleport)packet).getEntityID()) && ((String)PacketRecorder.this.entityLookup.get(((WrapperPlayServerEntityTeleport)packet).getEntityID())).equalsIgnoreCase(p.getName()) && (loc = PacketRecorder.this.checkEntityLocation(((WrapperPlayServerEntityTeleport)packet).getEntity(p.getWorld()))) != null) {
                        PacketRecorder.this.addData(p.getName(), new EntityMovingData(((WrapperPlayServerEntityTeleport)packet).getEntityID(), loc.getX(), loc.getY(), loc.getZ(), ((WrapperPlayServerEntityTeleport)packet).getPitch(), ((WrapperPlayServerEntityTeleport)packet).getYaw()));
                    }
                }
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)this.packetAdapter);
        this.registerExternalListeners();
    }

    @Override
    public void unregister() {
        super.unregister();
        ProtocolLibrary.getProtocolManager().removePacketListener((PacketListener)this.packetAdapter);
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


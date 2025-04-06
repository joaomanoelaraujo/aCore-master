/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerAction
 *  com.comphenix.protocol.wrappers.WrappedDataWatcher
 *  com.comphenix.protocol.wrappers.WrappedGameProfile
 *  com.comphenix.protocol.wrappers.WrappedSignedProperty
 *  org.bukkit.Bukkit
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Projectile
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity;
import com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

public class ReplayingUtils {
    private final Replayer replayer;
    private final Map<String, SignatureData> signatures;
    private final Deque<ActionData> lastSpawnActions;
    private final HashMap<Integer, Entity> itemEntities;
    private final HashMap<Integer, Integer> hooks;

    public ReplayingUtils(Replayer replayer) {
        this.replayer = replayer;
        this.itemEntities = new HashMap();
        this.hooks = new HashMap();
        this.lastSpawnActions = new ArrayDeque<ActionData>();
        this.signatures = new HashMap<String, SignatureData>();
    }

    public void handleAction(ActionData action, ReplayData data, boolean reversed) {
        INPC npc;
        if (action.getType() == ActionType.SPAWN) {
            if (!reversed) {
                this.spawnNPC(action);
            } else if (reversed && this.replayer.getNPCList().containsKey(action.getName())) {
                npc = this.replayer.getNPCList().get(action.getName());
                npc.remove();
                this.replayer.getNPCList().remove(action.getName());
            }
        }
        if (action.getType() == ActionType.MESSAGE && !reversed) {
            ChatData message = (ChatData)action.getPacketData();
            this.replayer.sendMessage(message.getMessage());
        }
        if (action.getType() == ActionType.PACKET && this.replayer.getNPCList().containsKey(action.getName())) {
            PacketData entityData;
            npc = this.replayer.getNPCList().get(action.getName());
            if (action.getPacketData() instanceof MovingData) {
                MovingData movingData = (MovingData)action.getPacketData();
                if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_15) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                    double distance = npc.getLocation().distance(new Location(npc.getOrigin().getWorld(), movingData.getX(), movingData.getY(), movingData.getZ()));
                    if (distance > 8.0) {
                        npc.teleport(new Location(npc.getOrigin().getWorld(), movingData.getX(), movingData.getY(), movingData.getZ()), true);
                    } else {
                        npc.move(new Location(npc.getOrigin().getWorld(), movingData.getX(), movingData.getY(), movingData.getZ()), true, movingData.getYaw(), movingData.getPitch());
                    }
                }
                if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_9, VersionUtil.VersionEnum.V1_14)) {
                    npc.teleport(new Location(npc.getOrigin().getWorld(), movingData.getX(), movingData.getY(), movingData.getZ()), true);
                    npc.look(movingData.getYaw(), movingData.getPitch());
                }
            }
            if (action.getPacketData() instanceof EntityActionData) {
                EntityActionData eaData = (EntityActionData)action.getPacketData();
                if (eaData.getAction() == EnumWrappers.PlayerAction.START_SNEAKING) {
                    data.getWatcher(action.getName()).setSneaking(!reversed);
                    npc.setData(data.getWatcher(action.getName()).getMetadata(new MetadataBuilder(npc.getData())));
                } else if (eaData.getAction() == EnumWrappers.PlayerAction.STOP_SNEAKING) {
                    data.getWatcher(action.getName()).setSneaking(reversed);
                    npc.setData(data.getWatcher(action.getName()).getMetadata(new MetadataBuilder(npc.getData())));
                }
                npc.updateMetadata();
            }
            if (action.getPacketData() instanceof AnimationData) {
                AnimationData animationData = (AnimationData)action.getPacketData();
                npc.animate(animationData.getId());
                if (animationData.getId() == 1 && !VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                    this.replayer.getWatchingPlayer().playSound(npc.getLocation(), Sound.CLICK, 5.0f, 5.0f);
                }
            }
            if (action.getPacketData() instanceof ChatData) {
                ChatData chatData = (ChatData)action.getPacketData();
                this.replayer.sendMessage(new MessageBuilder(ConfigManager.CHAT_FORMAT).set("name", action.getName()).set("message", chatData.getMessage()).build());
            }
            if (action.getPacketData() instanceof InvData) {
                InvData invData = (InvData)action.getPacketData();
                if (!VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                    List<WrapperPlayServerEntityEquipment> equipment = VersionUtil.isBelow(VersionUtil.VersionEnum.V1_15) ? NPCManager.updateEquipment(npc.getId(), invData) : NPCManager.updateEquipmentv16(npc.getId(), invData);

                    for (WrapperPlayServerEntityEquipment packet : equipment) {
                        packet.sendPacket(this.replayer.getWatchingPlayer());
                    }
                } else {
                    List<WrapperPlayServerEntityEquipment> equipment = NPCManager.updateEquipmentOld(npc.getId(), invData);
                    PacketNPCOld oldNPC = (PacketNPCOld)npc;
                    oldNPC.setLastEquipmentOld(equipment);
                    for (WrapperPlayServerEntityEquipment packet : equipment) {
                        packet.sendPacket(this.replayer.getWatchingPlayer());
                    }
                }
            }
            if (action.getPacketData() instanceof MetadataUpdate) {
                MetadataUpdate update = (MetadataUpdate)action.getPacketData();
                data.getWatcher(action.getName()).setBurning(!reversed && update.isBurning());
                data.getWatcher(action.getName()).setBlocking(!reversed && update.isBlocking());
                data.getWatcher(action.getName()).setElytra(!reversed && update.isGliding());
                data.getWatcher(action.getName()).setSwimming(!reversed && update.isSwimming());
                WrappedDataWatcher dataWatcher = data.getWatcher(action.getName()).getMetadata(new MetadataBuilder(npc.getData()));
                npc.setData(dataWatcher);
                npc.updateMetadata();
            }
            if (action.getPacketData() instanceof ProjectileData) {
                ProjectileData projectile = (ProjectileData)action.getPacketData();
                this.spawnProjectile(projectile, null, this.replayer.getWatchingPlayer().getWorld(), 0);
            }
            if (action.getPacketData() instanceof BlockChangeData) {
                BlockChangeData blockChange = (BlockChangeData)action.getPacketData();
                if (reversed) {
                    blockChange = new BlockChangeData(blockChange.getLocation(), blockChange.getAfter(), blockChange.getBefore());
                }
                this.setBlockChange(blockChange);
            }
            if (action.getPacketData() instanceof BedEnterData) {
                BedEnterData bed = (BedEnterData)action.getPacketData();
                if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14)) {
                    npc.teleport(LocationData.toLocation(bed.getLocation()), true);
                    npc.setData(new MetadataBuilder(npc.getData()).setPoseField("SLEEPING").getData());
                    npc.updateMetadata();
                    npc.teleport(LocationData.toLocation(bed.getLocation()), true);
                } else {
                    npc.sleep(LocationData.toLocation(bed.getLocation()));
                }
            }
            if (action.getPacketData() instanceof EntityItemData) {
                entityData = action.getPacketData();
                if (((EntityItemData)entityData).getAction() == 0 && !reversed) {
                    this.spawnItemStack((EntityItemData)entityData);
                } else if (((EntityItemData)entityData).getAction() == 1) {
                    if (this.itemEntities.containsKey(((EntityItemData)entityData).getId())) {
                        this.despawn(Collections.singletonList(this.itemEntities.get(((EntityItemData) entityData).getId())), null);
                        this.itemEntities.remove(((EntityItemData)entityData).getId());
                    }
                } else if (this.hooks.containsKey(((EntityItemData)entityData).getId())) {
                    this.despawn(null, new int[]{this.hooks.get(((EntityItemData)entityData).getId())});
                    this.hooks.remove(((EntityItemData)entityData).getId());
                }
            }
            if (action.getPacketData() instanceof EntityData) {
                entityData = action.getPacketData();
                if (((EntityData)entityData).getAction() == 0) {
                    if (!reversed) {
                        IEntity entity = VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8) ? new PacketEntityOld(EntityType.valueOf(((EntityData)entityData).getType())) : new PacketEntity(EntityType.valueOf(((EntityData)entityData).getType()));
                        entity.spawn(LocationData.toLocation(((EntityData)entityData).getLocation()), this.replayer.getWatchingPlayer());
                        this.replayer.getEntityList().put(((EntityData)entityData).getId(), entity);
                    } else if (this.replayer.getEntityList().containsKey(((EntityData)entityData).getId())) {
                        IEntity ent = this.replayer.getEntityList().get(((EntityData)entityData).getId());
                        ent.remove();
                    }
                } else if (((EntityData)entityData).getAction() == 1) {
                    if (!reversed && this.replayer.getEntityList().containsKey(((EntityData)entityData).getId())) {
                        IEntity ent = this.replayer.getEntityList().get(((EntityData)entityData).getId());
                        ent.remove();
                    } else {
                        IEntity entity = VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8) ? new PacketEntityOld(EntityType.valueOf(((EntityData)entityData).getType())) : new PacketEntity(EntityType.valueOf(((EntityData)entityData).getType()));
                        entity.spawn(LocationData.toLocation(((EntityData)entityData).getLocation()), this.replayer.getWatchingPlayer());
                        this.replayer.getEntityList().put(((EntityData)entityData).getId(), entity);
                    }
                }
            }
            if (action.getPacketData() instanceof EntityMovingData) {
                EntityMovingData entityMoving = (EntityMovingData)action.getPacketData();
                if (this.replayer.getEntityList().containsKey(entityMoving.getId())) {
                    IEntity ent = this.replayer.getEntityList().get(entityMoving.getId());
                    if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_15) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                        ent.move(new Location(ent.getOrigin().getWorld(), entityMoving.getX(), entityMoving.getY(), entityMoving.getZ()), true, entityMoving.getYaw(), entityMoving.getPitch());
                    }
                    if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_9, VersionUtil.VersionEnum.V1_14)) {
                        ent.teleport(new Location(ent.getOrigin().getWorld(), entityMoving.getX(), entityMoving.getY(), entityMoving.getZ()), true);
                        ent.look(entityMoving.getYaw(), entityMoving.getPitch());
                    }
                }
            }
            if (action.getPacketData() instanceof EntityAnimationData) {
                EntityAnimationData entityAnimating = (EntityAnimationData)action.getPacketData();
                if (this.replayer.getEntityList().containsKey(entityAnimating.getEntId()) && !reversed) {
                    IEntity ent = this.replayer.getEntityList().get(entityAnimating.getEntId());
                    ent.animate(entityAnimating.getId());
                }
            }
            if (action.getPacketData() instanceof WorldChangeData) {
                WorldChangeData worldChange = (WorldChangeData)action.getPacketData();
                Location loc = LocationData.toLocation(worldChange.getLocation());
                npc.despawn();
                npc.setOrigin(loc);
                npc.setLocation(loc);
                npc.respawn(this.replayer.getWatchingPlayer());
            }
            if (action.getPacketData() instanceof FishingData) {
                FishingData fishing = (FishingData)action.getPacketData();
                this.spawnProjectile(null, fishing, this.replayer.getWatchingPlayer().getWorld(), npc.getId());
            }
            if (action.getPacketData() instanceof VelocityData) {
                VelocityData velocity = (VelocityData)action.getPacketData();
                int entID = -1;
                if (this.hooks.containsKey(velocity.getId())) {
                    entID = this.hooks.get(velocity.getId());
                }
                if (this.replayer.getEntityList().containsKey(velocity.getId())) {
                    entID = this.replayer.getEntityList().get(velocity.getId()).getId();
                }
                if (entID != -1) {
                    WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity();
                    packet.setEntityID(entID);
                    packet.setVelocityX(velocity.getX());
                    packet.setVelocityY(velocity.getY());
                    packet.setVelocityZ(velocity.getZ());
                    packet.sendPacket(this.replayer.getWatchingPlayer());
                }
            }
        }
        if (action.getType() == ActionType.DESPAWN || action.getType() == ActionType.DEATH) {
            if (!reversed && this.replayer.getNPCList().containsKey(action.getName())) {
                npc = this.replayer.getNPCList().get(action.getName());
                npc.remove();
                this.replayer.getNPCList().remove(action.getName());
                SpawnData oldSpawnData = new SpawnData(npc.getUuid(), LocationData.fromLocation(npc.getLocation()), this.signatures.get(action.getName()));
                this.lastSpawnActions.addLast(new ActionData(0, ActionType.SPAWN, action.getName(), oldSpawnData));
                if (action.getType() == ActionType.DESPAWN) {
                    this.replayer.sendMessage(new MessageBuilder(ConfigManager.LEAVE_MESSAGE).set("name", action.getName()).build());
                } else {
                    this.replayer.sendMessage(new MessageBuilder(ConfigManager.DEATH_MESSAGE).set("name", action.getName()).build());
                }
            } else if (!this.lastSpawnActions.isEmpty()) {
                this.spawnNPC(this.lastSpawnActions.pollLast());
            }
        }
    }

    public void forward() {
        boolean paused = this.replayer.isPaused();
        this.replayer.setPaused(true);
        int currentTick = this.replayer.getCurrentTicks();
        int forwardTicks = currentTick + 200;
        int duration = this.replayer.getReplay().getData().getDuration();
        if (forwardTicks + 2 >= duration) {
            forwardTicks = duration - 20;
        }
        for (int i = currentTick; i < forwardTicks; ++i) {
            this.replayer.executeTick(i, false);
        }
        this.replayer.setCurrentTicks(forwardTicks);
        this.replayer.setPaused(paused);
    }

    public void backward() {
        boolean paused = this.replayer.isPaused();
        this.replayer.setPaused(true);
        int currentTick = this.replayer.getCurrentTicks();
        int backwardTicks = currentTick - 200;
        if (backwardTicks - 2 <= 0) {
            backwardTicks = 1;
        }
        for (int i = currentTick; i > backwardTicks; --i) {
            this.replayer.executeTick(i, true);
        }
        this.replayer.setCurrentTicks(backwardTicks);
        this.replayer.setPaused(paused);
    }

    public void jumpTo(Integer seconds) {
        int targetTicks = seconds * 20;
        int currentTick = this.replayer.getCurrentTicks();
        if (currentTick > targetTicks) {
            this.replayer.setPaused(true);
            if (targetTicks - 2 > 0) {
                for (int i = currentTick; i > targetTicks; --i) {
                    this.replayer.executeTick(i, true);
                }
                this.replayer.setCurrentTicks(targetTicks);
                this.replayer.setPaused(false);
            }
        } else if (currentTick < targetTicks) {
            this.replayer.setPaused(true);
            int duration = this.replayer.getReplay().getData().getDuration();
            if (targetTicks + 2 < duration) {
                for (int i = currentTick; i < targetTicks; ++i) {
                    this.replayer.executeTick(i, false);
                }
                this.replayer.setCurrentTicks(targetTicks);
                this.replayer.setPaused(false);
            }
        }
    }

    private void spawnNPC(ActionData action) {
        int tabMode;
        SpawnData spawnData = (SpawnData)action.getPacketData();
        int n = tabMode = Bukkit.getPlayer(action.getName()) != null ? 0 : 2;
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14) && Bukkit.getPlayer(action.getName()) != null) {
            tabMode = 2;
            spawnData.setUuid(UUID.randomUUID());
        }
        INPC npc = !VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8) ? new PacketNPC(MathUtils.randInt(10000, 20000), spawnData.getUuid(), action.getName()) : new PacketNPCOld(MathUtils.randInt(10000, 20000), spawnData.getUuid(), action.getName());
        this.replayer.getNPCList().put(action.getName(), npc);
        this.replayer.getReplay().getData().getWatchers().put(action.getName(), new PlayerWatcher(action.getName()));
        Location spawn = LocationData.toLocation(spawnData.getLocation());
        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            npc.setData(new MetadataBuilder(this.replayer.getWatchingPlayer()).resetValue().getData());
        } else {
            npc.setData(new MetadataBuilder(this.replayer.getWatchingPlayer()).setArrows(0).resetValue().getData());
        }
        if (ConfigManager.HIDE_PLAYERS && !action.getName().equals(this.replayer.getWatchingPlayer().getName())) {
            tabMode = 2;
        }
        if (spawnData.getSignature() != null && (Bukkit.getPlayer(action.getName()) == null || VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14)) || spawnData.getSignature() != null && ConfigManager.HIDE_PLAYERS && !action.getName().equals(this.replayer.getWatchingPlayer().getName())) {
            WrappedGameProfile profile = new WrappedGameProfile(spawnData.getUuid(), action.getName());
            WrappedSignedProperty signed = new WrappedSignedProperty(spawnData.getSignature().getName(), spawnData.getSignature().getValue(), spawnData.getSignature().getSignature());
            profile.getProperties().put(spawnData.getSignature().getName(), signed);
            npc.setProfile(profile);
            if (!this.signatures.containsKey(action.getName())) {
                this.signatures.put(action.getName(), spawnData.getSignature());
            }
        }
        npc.spawn(spawn, tabMode, this.replayer.getWatchingPlayer());
        npc.look(spawnData.getLocation().getYaw(), spawnData.getLocation().getPitch());
    }

    private void spawnProjectile(final ProjectileData projData, FishingData fishing, final World world, int id) {
        if (projData != null && projData.getType() != EntityType.FISHING_HOOK) {
            if (projData.getType() == EntityType.ENDER_PEARL && VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
                return;
            }
            new BukkitRunnable(){

                public void run() {
                    Projectile proj = (Projectile)world.spawnEntity(LocationData.toLocation(projData.getSpawn()), projData.getType());
                    proj.setVelocity(LocationData.toLocation(projData.getVelocity()).toVector());
                }
            }.runTask(Core.getInstance());
        }
        if (fishing != null) {
            int rndID = MathUtils.randInt(2000, 30000);
            AbstractPacket packet = VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8) ? FishingUtils.createHookPacketOld(fishing, id, rndID) : FishingUtils.createHookPacket(fishing, id, rndID);
            this.hooks.put(fishing.getId(), rndID);
            packet.sendPacket(this.replayer.getWatchingPlayer());
        }
    }

    private void setBlockChange(final BlockChangeData blockChange) {
        final Location loc = LocationData.toLocation(blockChange.getLocation());
        if (ConfigManager.WORLD_RESET && !this.replayer.getBlockChanges().containsKey(loc)) {
            this.replayer.getBlockChanges().put(loc, blockChange.getBefore());
        }
        new BukkitRunnable(){

            public void run() {
                if (blockChange.getAfter().getId() == 0 && blockChange.getBefore().getId() != 0 && MaterialBridge.fromID(blockChange.getBefore().getId()) != Material.FIRE && blockChange.getBefore().getId() != 11 && blockChange.getBefore().getId() != 9 && blockChange.getBefore().getId() != 10 && blockChange.getBefore().getId() != 8) {
                    loc.getWorld().playEffect(loc, Effect.STEP_SOUND, blockChange.getBefore().getId(), 15);
                }
                int id = blockChange.getAfter().getId();
                int subId = blockChange.getAfter().getSubId();
                if (id == 9) {
                    id = 8;
                }
                if (id == 11) {
                    id = 10;
                }
                if (ConfigManager.REAL_CHANGES) {
                    if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_13) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_14) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_15) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_16) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_17) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_18) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_19)) {
                        loc.getBlock().setType(ReplayingUtils.this.getBlockMaterial(blockChange.getAfter()), true);
                    } else {
                        loc.getBlock().setTypeIdAndData(id, (byte)subId, true);
                    }
                } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_13) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_14) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_15) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_16) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_17) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_18) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_19)) {
                    ReplayingUtils.this.replayer.getWatchingPlayer().sendBlockChange(loc, ReplayingUtils.this.getBlockMaterial(blockChange.getAfter()), (byte)subId);
                } else {
                    ReplayingUtils.this.replayer.getWatchingPlayer().sendBlockChange(loc, id, (byte)subId);
                }
            }
        }.runTask(Core.getInstance());
    }

    private Material getBlockMaterial(ItemData data) {
        if (data.getItemStack() != null) {
            return MaterialBridge.getWithoutLegacy(String.valueOf(data.getItemStack().getItemStack().get("type")));
        }
        return MaterialBridge.fromID(data.getId());
    }

    private void spawnItemStack(final EntityItemData entityData) {
        final Location loc = LocationData.toLocation(entityData.getLocation());
        new BukkitRunnable(){

            public void run() {
                Item item = loc.getWorld().dropItemNaturally(loc, NPCManager.fromID(entityData.getItemData()));
                item.setVelocity(LocationData.toLocation(entityData.getVelocity()).toVector());
                ReplayingUtils.this.itemEntities.put(entityData.getId(), item);
            }
        }.runTask(Core.getInstance());
    }

    public void despawn(final List<Entity> entities, int[] ids) {
        if (entities != null && !entities.isEmpty()) {
            new BukkitRunnable() {
                public void run() {
                    for (Entity en : entities) {
                        if (en != null) {
                            en.remove();
                        }
                    }
                }
            }.runTask(Core.getInstance());
        }

        if (ids != null && ids.length > 0) {
            WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();

            // Versões acima de 1.17
            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
                // Para versões mais recentes, defina o pacote de acordo com a documentação
                packet.setEntityIds(ids);
            } else {
                // Para versões anteriores
                packet.setEntityIds(ids);
            }

            packet.sendPacket(this.replayer.getWatchingPlayer());
        }
    }


    public void resetChanges(Map<Location, ItemData> changes) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(Core.getInstance(), () -> this.setBlocks(changes));
        } else {
            this.setBlocks(changes);
        }
    }

    private void setBlocks(Map<Location, ItemData> changes) {
        changes.forEach((location, itemData) -> {
            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13)) {
                location.getBlock().setType(this.getBlockMaterial(itemData));
            } else {
                location.getBlock().setTypeIdAndData(itemData.getId(), (byte)itemData.getSubId(), true);
            }
        });
    }

    public HashMap<Integer, Entity> getEntities() {
        return this.itemEntities;
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerBedEnterEvent
 *  org.bukkit.event.player.PlayerBedLeaveEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerBucketFillEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.ItemStack
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class RecordingListener
extends AbstractListener {
    private final PacketRecorder packetRecorder;
    private final Recorder recorder;
    private final List<String> replayLeft;

    public RecordingListener(PacketRecorder packetRecorder) {
        this.packetRecorder = packetRecorder;
        this.recorder = this.packetRecorder.getRecorder();
        this.replayLeft = new ArrayList<>();
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player)e.getWhoClicked();
            if (this.recorder.getPlayers().contains(p.getName())) {
                this.packetRecorder.addData(p.getName(), NPCManager.copyFromPlayer(p, true, true));
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            ItemStack stack = p.getInventory().getItem(e.getNewSlot());
            this.itemInHand(p, stack);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                boolean isInteractable;
                boolean bl = isInteractable = e.getClickedBlock() != null && ItemUtils.isInteractable(e.getClickedBlock().getType());
                if (e.getItem() != null && ItemUtils.isUsable(e.getItem().getType()) && (!isInteractable || p.isSneaking()) && !this.recorder.getData().getWatcher(p.getName()).isBlocking()) {
                    PlayerWatcher watcher = this.recorder.getData().getWatcher(p.getName());
                    watcher.setBlocking(true);
                    this.packetRecorder.addData(p.getName(), MetadataUpdate.fromWatcher(watcher));
                }
                if (NPCManager.isArmor(e.getItem()) && !NPCManager.wearsArmor(p, NPCManager.getArmorType(e.getItem()))) {
                    InvData data = NPCManager.copyFromPlayer(p, true, true);
                    String armorType = NPCManager.getArmorType(e.getItem());
                    if (armorType != null) {
                        if (armorType.equals("head")) {
                            data.setHead(NPCManager.fromItemStack(e.getItem()));
                        }
                        if (armorType.equals("chest")) {
                            data.setChest(NPCManager.fromItemStack(e.getItem()));
                        }
                        if (armorType.equals("leg")) {
                            data.setLeg(NPCManager.fromItemStack(e.getItem()));
                        }
                        if (armorType.equals("boots")) {
                            data.setBoots(NPCManager.fromItemStack(e.getItem()));
                        }
                    }
                    if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                        data.setMainHand(null);
                    }
                    this.packetRecorder.addData(p.getName(), data);
                }
            }
            if (e.getAction() == Action.LEFT_CLICK_BLOCK && p.getTargetBlock((Set)null, 5).getType() == Material.FIRE) {
                LocationData location = LocationData.fromLocation(p.getTargetBlock((Set)null, 5).getLocation());
                ItemData before = new ItemData(Material.FIRE.getId(), 0);
                ItemData after = new ItemData(0, 0);
                this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName()) && this.recorder.getData().getWatcher(p.getName()).isBlocking()) {
            PlayerWatcher watcher = this.recorder.getData().getWatcher(p.getName());
            watcher.setBlocking(false);
            this.packetRecorder.addData(p.getName(), MetadataUpdate.fromWatcher(watcher));
            InvData data = NPCManager.copyFromPlayer(p, true, true);
            if (p.getItemInHand() != null && p.getItemInHand().getAmount() <= 1) {
                data.setMainHand(null);
            }
            this.packetRecorder.addData(p.getName(), data);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            if (this.recorder.getPlayers().contains(p.getName())) {
                this.packetRecorder.addData(p.getName(), new AnimationData(1));
                if (p.getFireTicks() > 20 && !this.recorder.getData().getWatcher(p.getName()).isBurning()) {
                    this.recorder.getData().getWatcher(p.getName()).setBurning(true);
                    this.packetRecorder.addData(p.getName(), new MetadataUpdate(true, this.recorder.getData().getWatcher(p.getName()).isBlocking(), this.recorder.getData().getWatcher(p.getName()).isElytra()));
                } else if (p.getFireTicks() <= 20 && this.recorder.getData().getWatcher(p.getName()).isBurning()) {
                    this.recorder.getData().getWatcher(p.getName()).setBurning(false);
                    this.packetRecorder.addData(p.getName(), new MetadataUpdate(false, this.recorder.getData().getWatcher(p.getName()).isBlocking(), this.recorder.getData().getWatcher(p.getName()).isElytra()));
                }
            }
        } else if (e.getEntity() instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)e.getEntity();
            if (this.packetRecorder.getEntityLookup().containsKey(living.getEntityId())) {
                this.packetRecorder.addData(this.packetRecorder.getEntityLookup().get(living.getEntityId()), new EntityAnimationData(living.getEntityId(), living.getHealth() - e.getFinalDamage() > 0.0 ? 2 : 3));
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onCrit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player damager = (Player)e.getDamager();
            Player victim = (Player)e.getEntity();
            if (this.recorder.getPlayers().contains(damager.getName()) && this.recorder.getPlayers().contains(victim.getName()) && damager.getFallDistance() > 0.0f && !damager.isOnGround() && damager.getVehicle() == null) {
                this.packetRecorder.addData(victim.getName(), new AnimationData(4));
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            this.packetRecorder.addData(p.getName(), new ChatData(e.getMessage()));
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onBed(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            this.packetRecorder.addData(p.getName(), new BedEnterData(LocationData.fromLocation(e.getBed().getLocation())));
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onBedLeave(PlayerBedLeaveEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            this.packetRecorder.addData(p.getName(), new AnimationData(2));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            this.recorder.addData(this.recorder.getCurrentTick(), new ActionData(0, ActionType.DESPAWN, p.getName(), null));
            this.recorder.getPlayers().remove(p.getName());
            if (!this.replayLeft.contains(p.getName())) {
                this.replayLeft.add(p.getName());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!this.recorder.getPlayers().contains(p.getName()) && this.replayLeft.contains(p.getName()) || ConfigManager.ADD_PLAYERS) {
            this.recorder.getPlayers().add(p.getName());
            this.recorder.getData().getWatchers().put(p.getName(), new PlayerWatcher(p.getName()));
            this.recorder.createSpawnAction(p, p.getLocation(), false);
            this.recorder.addData(this.recorder.getCurrentTick(), new ActionData(this.recorder.getCurrentTick(), ActionType.MESSAGE, p.getName(), new ChatData(new MessageBuilder(ConfigManager.JOIN_MESSAGE).set("name", p.getName()).build())));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (this.recorder.getPlayers().contains(p.getName())) {
            this.recorder.addData(this.recorder.getCurrentTick(), new ActionData(0, ActionType.DEATH, p.getName(), null));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            this.recorder.createSpawnAction(p, e.getRespawnLocation(), false);
        }
    }

    @EventHandler
    public void onThrow(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            InvData data = NPCManager.copyFromPlayer(p, true, true);
            if (data.getMainHand() != null && p.getItemInHand() != null && p.getItemInHand().getAmount() <= 1 && p.getItemInHand().getType() == MaterialBridge.fromID(data.getMainHand().getId())) {
                data.setMainHand(null);
            }
            this.packetRecorder.addData(p.getName(), data);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onLaunch(ProjectileLaunchEvent e) {
        Projectile proj = e.getEntity();
        if (proj.getShooter() instanceof Player) {
            Player p = (Player)proj.getShooter();
            if (this.recorder.getPlayers().contains(p.getName())) {
                LocationData spawn = LocationData.fromLocation(p.getEyeLocation());
                LocationData velocity = LocationData.fromLocation(proj.getVelocity().toLocation(p.getWorld()));
                this.packetRecorder.addData(p.getName(), new ProjectileData(spawn, velocity, proj.getType()));
                this.packetRecorder.addData(p.getName(), NPCManager.copyFromPlayer(p, true, true));
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            this.itemInHand(p, e.getItem().getItemStack());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            LocationData location = LocationData.fromLocation(e.getBlockPlaced().getLocation());
            ItemData before = new ItemData(e.getBlockReplacedState().getType().getId(), e.getBlockReplacedState().getData().getData());
            ItemData after = VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13) ? new ItemData(SerializableItemStack.fromMaterial(MaterialBridge.getBlockDataMaterial(e.getBlockPlaced()))) : new ItemData(e.getBlockPlaced().getType().getId(), e.getBlockPlaced().getData());
            this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
            if (e.getItemInHand().getAmount() == 1) {
                ItemStack stack = new ItemStack(Material.AIR, 1);
                this.itemInHand(p, stack);
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            LocationData location = LocationData.fromLocation(e.getBlock().getLocation());
            ItemData before = VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13) ? new ItemData(SerializableItemStack.fromMaterial(MaterialBridge.getBlockDataMaterial(e.getBlock()))) : new ItemData(e.getBlock().getType().getId(), e.getBlock().getData());
            ItemData after = new ItemData(0, 0);
            this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onFill(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            LocationData location = LocationData.fromLocation(e.getBlockClicked().getLocation());
            ItemData before = new ItemData(e.getBlockClicked().getState().getType().getId(), e.getBlockClicked().getState().getData().getData());
            ItemData after = new ItemData(0, 0);
            this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
            ItemStack stack = e.getBlockClicked().getState().getType().getId() == 10 || e.getBlockClicked().getState().getType().getId() == 11 ? new ItemStack(Material.LAVA_BUCKET, 1) : new ItemStack(Material.WATER_BUCKET, 1);
            this.itemInHand(p, stack);
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            Block block = e.getBlockClicked().getRelative(e.getBlockFace());
            LocationData location = LocationData.fromLocation(block.getLocation());
            ItemData before = new ItemData(block.getType().getId(), block.getData());
            ItemData after = new ItemData(e.getBucket() == Material.LAVA_BUCKET ? 11 : 9, 0);
            this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
            ItemStack stack = new ItemStack(Material.BUCKET, 1);
            this.itemInHand(p, stack);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        if (this.recorder.getPlayers().contains(p.getName())) {
            LocationData location = LocationData.fromLocation(p.getLocation());
            this.packetRecorder.addData(p.getName(), new WorldChangeData(location));
        }
    }

    public void itemInHand(Player p, ItemStack stack) {
        InvData data = NPCManager.copyFromPlayer(p, true, true);
        data.setMainHand(NPCManager.fromItemStack(stack));
        this.packetRecorder.addData(p.getName(), data);
        if (this.recorder.getData().getWatcher(p.getName()).isBlocking()) {
            this.recorder.getData().getWatcher(p.getName()).setBlocking(false);
            this.packetRecorder.addData(p.getName(), new MetadataUpdate(this.recorder.getData().getWatcher(p.getName()).isBurning(), false, this.recorder.getData().getWatcher(p.getName()).isElytra()));
        }
    }
}


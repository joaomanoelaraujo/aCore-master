/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerToggleSneakEvent
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

import java.util.Arrays;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ReplayListener
extends AbstractListener {
    @EventHandler(priority=EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent e) {
        Player p;
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && ReplayHelper.replaySessions.containsKey((p = e.getPlayer()).getName())) {
            e.setCancelled(true);
            Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
            if (p.getItemInHand() == null) {
                return;
            }
            if (p.getItemInHand().getItemMeta() == null) {
                return;
            }
            ItemMeta meta = p.getItemInHand().getItemMeta();
            ItemConfigType itemType = ItemConfig.getByIdAndName(p.getItemInHand().getType(), meta.getDisplayName().replaceAll("§", "&"));
            if (itemType == ItemConfigType.PAUSE) {
                replayer.setPaused(!replayer.isPaused());
                ReplayHelper.sendTitle(p, " ", "§c\u2759\u2759", 20);
            }
            if (itemType == ItemConfigType.FORWARD) {
                replayer.getUtils().forward();
                ReplayHelper.sendTitle(p, " ", "§a\u00bb\u00bb", 20);
            }
            if (itemType == ItemConfigType.BACKWARD) {
                replayer.getUtils().backward();
                ReplayHelper.sendTitle(p, " ", "§c\u00ab\u00ab", 20);
            }
            if (itemType == ItemConfigType.RESUME) {
                replayer.setPaused(!replayer.isPaused());
                ReplayHelper.sendTitle(p, " ", "§a\u27a4", 20);
            }
            if (itemType == ItemConfigType.SPEED) {
                if (p.isSneaking()) {
                    if (replayer.getSpeed() < 1.0) {
                        replayer.setSpeed(1.0);
                    } else if (replayer.getSpeed() == 1.0) {
                        replayer.setSpeed(2.0);
                    }
                } else if (replayer.getSpeed() == 2.0) {
                    replayer.setSpeed(1.0);
                } else if (replayer.getSpeed() == 1.0) {
                    replayer.setSpeed(0.5);
                } else if (replayer.getSpeed() == 0.5) {
                    replayer.setSpeed(0.25);
                }
            }
            if (itemType == ItemConfigType.LEAVE) {
                replayer.stop();
            }
            if (itemType == ItemConfigType.TELEPORT) {
                ReplayHelper.createTeleporter(p, replayer);
            }
            ItemConfigOption pauseResume = ItemConfig.getItem(ItemConfigType.RESUME);
            if (itemType == ItemConfigType.PAUSE || itemType == ItemConfigType.RESUME) {
                if (replayer.isPaused()) {
                    p.getInventory().setItem(pauseResume.getSlot(), ReplayHelper.getResumeItem());
                } else {
                    p.getInventory().setItem(pauseResume.getSlot(), ReplayHelper.getPauseItem());
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p;
        if (e.getWhoClicked() instanceof Player && ReplayHelper.replaySessions.containsKey((p = (Player)e.getWhoClicked()).getName())) {
            e.setCancelled(true);
            if (e.getView().getTitle().equalsIgnoreCase("§7Teleporter")) {
                Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
                if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getType().getId() == 397) {
                    String owner = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", "");
                    if (replayer.getNPCList().containsKey(owner)) {
                        INPC npc = replayer.getNPCList().get(owner);
                        p.teleport(npc.getLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        Player p = (Player)e.getEntity();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            e.setFoodLevel(20);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Player p;
        if (e.getEntity() instanceof Player && ReplayHelper.replaySessions.containsKey((p = (Player)e.getEntity()).getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
            for (INPC npc : replayer.getNPCList().values()) {
                npc.despawn();
                npc.respawn(p);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
            replayer.stop();
            replayer.getSession().resetPlayer();
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        boolean isReplayItem;
        Player p = e.getPlayer();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            e.setCancelled(true);
        }
        if (isReplayItem = ReplayHelper.replaySessions.values().stream().anyMatch(replayer -> replayer.getUtils().getEntities().containsValue(e.getItem()))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            Chunk oldChunk = e.getFrom().getChunk();
            Chunk newChunk = e.getTo().getChunk();
            if (oldChunk.getWorld() != newChunk.getWorld() || oldChunk.getX() != newChunk.getX() || oldChunk.getZ() != newChunk.getZ()) {
                Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
                for (INPC npc : replayer.getNPCList().values()) {
                    if (ReplayHelper.isInRange(npc.getLocation(), p.getLocation())) {
                        if (Arrays.asList(npc.getVisible()).contains(p)) continue;
                        npc.respawn(p);
                        continue;
                    }
                    if (!Arrays.asList(npc.getVisible()).contains(p)) continue;
                    npc.despawn();
                }
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        final Player p = e.getPlayer();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            final Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
            new BukkitRunnable(){

                public void run() {
                    for (INPC npc : replayer.getNPCList().values()) {
                        npc.despawn();
                        if (!ReplayHelper.isInRange(p.getLocation(), npc.getLocation())) continue;
                        npc.respawn(p);
                    }
                }
            }.runTaskLater((Plugin)Core.getInstance(), 20L);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        ReplayPacketListener packetListener;
        Player p = e.getPlayer();
        if (ReplayHelper.replaySessions.containsKey(p.getName()) && (packetListener = ReplayHelper.replaySessions.get(p.getName()).getSession().getPacketListener()).getPrevious() != -1) {
            packetListener.setCamera(p, p.getEntityId(), packetListener.getPrevious());
            p.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            e.setKeepLevel(true);
            e.setKeepInventory(true);
        }
    }
}


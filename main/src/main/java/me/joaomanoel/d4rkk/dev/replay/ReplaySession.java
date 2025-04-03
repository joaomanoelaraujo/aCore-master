/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ReplaySession {
    private final Replayer replayer;
    private final Player player;
    private ItemStack[] content;
    private int level;
    private float xp;
    private Location start;
    private final ReplayPacketListener packetListener;

    public ReplaySession(Replayer replayer) {
        this.replayer = replayer;
        this.player = this.replayer.getWatchingPlayer();
        this.packetListener = new ReplayPacketListener(replayer);
    }

    public void startSession() {
        this.content = this.player.getInventory().getContents();
        if (this.start == null) {
            this.start = this.player.getLocation();
        }
        this.level = this.player.getLevel();
        this.xp = this.player.getExp();
        this.player.setHealth(this.player.getMaxHealth());
        this.player.setFoodLevel(20);
        this.player.getInventory().clear();
        ItemConfigOption teleport = ItemConfig.getItem(ItemConfigType.TELEPORT);
        ItemConfigOption time = ItemConfig.getItem(ItemConfigType.SPEED);
        ItemConfigOption leave = ItemConfig.getItem(ItemConfigType.LEAVE);
        ItemConfigOption backward = ItemConfig.getItem(ItemConfigType.BACKWARD);
        ItemConfigOption forward = ItemConfig.getItem(ItemConfigType.FORWARD);
        ItemConfigOption pauseResume = ItemConfig.getItem(ItemConfigType.PAUSE);
        List<ItemConfigOption> configItems = Arrays.asList(teleport, time, leave, backward, forward, pauseResume);
        configItems.stream().filter(ItemConfigOption::isEnabled).forEach(item -> this.player.getInventory().setItem(item.getSlot(), ReplayHelper.createItem(item)));
        this.player.setAllowFlight(true);
        this.player.setFlying(true);
        if (ConfigManager.HIDE_PLAYERS) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all == this.player) continue;
                this.player.hidePlayer(all);
            }
        }
    }

    public void stopSession() {
        ReplayHelper.replaySessions.remove(this.player.getName());
        this.packetListener.unregister();
        new BukkitRunnable(){

            public void run() {
                ReplaySession.this.resetPlayer();
                ReplaySession.this.player.teleport(ReplaySession.this.start);
                if (ConfigManager.HIDE_PLAYERS) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (all == ReplaySession.this.player) continue;
                        ReplaySession.this.player.showPlayer(all);
                    }
                }
                ReplaySessionFinishEvent finishEvent = new ReplaySessionFinishEvent(ReplaySession.this.replayer.getReplay(), ReplaySession.this.player);
                Bukkit.getPluginManager().callEvent(finishEvent);
            }
        }.runTask(Core.getInstance());
    }

    public void resetPlayer() {
        this.player.getInventory().clear();
        this.player.getInventory().setContents(this.content);
        if (this.player.getGameMode() != GameMode.CREATIVE) {
            this.player.setFlying(false);
            this.player.setAllowFlight(false);
        }
        this.player.setLevel(this.level);
        this.player.setExp(this.xp);
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public ReplayPacketListener getPacketListener() {
        return this.packetListener;
    }
}


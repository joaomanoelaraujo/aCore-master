/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.hotbar.Hotbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Replayer {
    private HashMap<String, INPC> npcs;
    private HashMap<Integer, IEntity> entities;
    private Map<Location, ItemData> blockChanges;
    private Player watcher;
    private Replay replay;
    private BukkitRunnable run;
    private int currentTicks;
    private double speed;
    private double tmpTicks;
    private boolean paused;
    private boolean started;
    private ReplayingUtils utils;
    private ReplaySession session;

    public Replayer(Replay replay, Player watcher) {
        this.replay = replay;
        this.watcher = watcher;
        this.npcs = new HashMap();
        this.entities = new HashMap();
        this.blockChanges = new HashMap<Location, ItemData>();
        this.utils = new ReplayingUtils(this);
        this.session = new ReplaySession(this);
        this.paused = false;
        ReplayHelper.replaySessions.put(watcher.getName(), this);
    }

    public void start() {
        ReplayData data = this.replay.getData();
        final int duration = data.getDuration();
        this.session.setStart(this.watcher.getLocation());
        if (data.getActions().containsKey(0)) {
            for (ActionData startData : data.getActions().get(0)) {
                if (!(startData.getPacketData() instanceof SpawnData)) continue;
                SpawnData spawnData = (SpawnData)startData.getPacketData();
                this.watcher.teleport(LocationData.toLocation(spawnData.getLocation()));
                break;
            }
        } else {
            Optional<SpawnData> spawnData = this.findFirstSpawn(data);
            if (spawnData.isPresent()) {
                this.watcher.teleport(LocationData.toLocation(spawnData.get().getLocation()));
            }
        }
        this.session.startSession();
        this.speed = 1.0;
        this.executeTick(0, false);
        this.run = new BukkitRunnable(){

            public void run() {
                if (Replayer.this.paused) {
                    return;
                }
                Replayer replayer = Replayer.this;
                replayer.tmpTicks = replayer.tmpTicks + Replayer.this.speed;
                if (Replayer.this.tmpTicks % 1.0 != 0.0) {
                    return;
                }
                if (Replayer.this.currentTicks < duration) {
                    Replayer.this.executeTick(Replayer.this.currentTicks++, false);
                    if (Replayer.this.currentTicks + 2 < duration && Replayer.this.speed == 2.0) {
                        Replayer.this.executeTick(Replayer.this.currentTicks++, false);
                    }
                    Replayer.this.updateXPBar();
                } else {
                    Replayer.this.stop();
                }
            }
        };
        this.run.runTaskTimerAsynchronously((Plugin)Core.getInstance(), 1L, 1L);
    }

    public void executeTick(int tick, boolean reversed) {
        ReplayData data = this.replay.getData();
        if (!data.getActions().isEmpty() && data.getActions().containsKey(tick)) {
            if (tick == 0 && this.started) {
                return;
            }
            this.started = true;
            List<ActionData> list = data.getActions().get(tick);
            for (ActionData action : list) {
                this.utils.handleAction(action, data, reversed);
                if (action.getType() != ActionType.CUSTOM || !ReplayAPI.getInstance().getHookManager().isRegistered()) continue;
                for (IReplayHook hook : ReplayAPI.getInstance().getHookManager().getHooks()) {
                    hook.onPlay(action, this);
                }
            }
            if (tick == 0) {
                data.getActions().remove(tick);
            }
        }
    }

    private void updateXPBar() {
        int level = this.currentTicks / 20;
        float percentage = (float)this.currentTicks / (float)this.replay.getData().getDuration();
        this.watcher.setLevel(level);
        this.watcher.setExp(percentage);
    }

    private Optional<SpawnData> findFirstSpawn(ReplayData data) {
        return data.getActions().values().stream().flatMap(Collection::stream).filter(action -> action.getPacketData() instanceof SpawnData).map(action -> (SpawnData)action.getPacketData()).findFirst();
    }

    public void stop() {
        Profile profile = Profile.getProfile(this.watcher.getName());
        profile.setHotbar(Hotbar.getHotbarById("lobby"));
        profile.refresh();
        this.run.cancel();
        this.getReplay().getData().getActions().clear();
        for (INPC npc : this.npcs.values()) {
            npc.remove();
        }
        for (IEntity entity : this.entities.values()) {
            entity.remove();
        }
        this.utils.despawn(new ArrayList<Entity>(this.utils.getEntities().values()), null);
        this.npcs.clear();
        this.replay.setPlaying(false);
        if (ConfigManager.WORLD_RESET) {
            this.utils.resetChanges(this.blockChanges);
        }
        this.session.stopSession();
    }

    public HashMap<String, INPC> getNPCList() {
        return this.npcs;
    }

    public HashMap<Integer, IEntity> getEntityList() {
        return this.entities;
    }

    public Map<Location, ItemData> getBlockChanges() {
        return this.blockChanges;
    }

    public Player getWatchingPlayer() {
        return this.watcher;
    }

    public Replay getReplay() {
        return this.replay;
    }

    public ReplayingUtils getUtils() {
        return this.utils;
    }

    public ReplaySession getSession() {
        return this.session;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setSpeed(double speed) {
        this.tmpTicks = 1.0;
        this.speed = speed;
        ReplayHelper.sendTitle(this.watcher, " ", speed >= 1.0 ? "§ax" + speed : "§cx" + speed, 10);
    }

    public double getSpeed() {
        return this.speed;
    }

    public int getCurrentTicks() {
        return this.currentTicks;
    }

    public void setCurrentTicks(int currentTicks) {
        this.currentTicks = currentTicks;
    }

    public void sendMessage(String message) {
        if (message != null) {
            this.watcher.sendMessage(message);
        }
    }
}


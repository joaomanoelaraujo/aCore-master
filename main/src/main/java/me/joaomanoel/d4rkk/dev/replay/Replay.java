/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Replay {
    private String id;
    private ReplayData data;
    private ReplayInfo replayInfo;
    private Recorder recorder;
    private Replayer replayer;
    private boolean isRecording;
    private boolean isPlaying;

    public Replay() {
        this.id = StringUtils.getRandomString(6);
        this.data = new ReplayData();
        this.isRecording = false;
        this.isPlaying = false;
    }

    public Replay(String id, ReplayData data) {
        this.id = id;
        this.data = data;
    }

    public void record(Player ... players) {
        this.recordAll(Arrays.asList(players));
    }

    public void recordAll(List<Player> players) {
        this.recorder = new Recorder(this, players);
        this.recorder.start();
        this.isRecording = true;
        ReplayManager.activeReplays.put(this.id, this);
    }

    public void play(Player watcher) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask((Plugin)Core.getInstance(), () -> this.startReplay(watcher));
        } else {
            this.startReplay(watcher);
        }
    }

    private void startReplay(Player watcher) {
        this.replayer = new Replayer(this, watcher);
        this.replayer.start();
        this.isPlaying = true;
    }

    public String getId() {
        return this.id;
    }

    public ReplayData getData() {
        return this.data;
    }

    public void setData(ReplayData data) {
        this.data = data;
    }

    public Recorder getRecorder() {
        return this.recorder;
    }

    public Replayer getReplayer() {
        return this.replayer;
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRecording(boolean recording) {
        this.isRecording = recording;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void setReplayInfo(ReplayInfo replayInfo) {
        this.replayInfo = replayInfo;
    }

    public ReplayInfo getReplayInfo() {
        return this.replayInfo;
    }
}


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

public class Replay {
    private String id;
    private ReplayData data;
    private ReplayInfo replayInfo;
    private Recorder recorder;
    private Replayer replayer;
    private boolean isRecording;
    private boolean isPlaying;

    private String gameName;
    private String mode;
    private String map;
    private String server;
    private int players;

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

    public void record(Player... players) {
        this.recordAll(Arrays.asList(players));
    }

    public void recordAll(List<Player> players) {
        this.recorder = new Recorder(
                this,
                players,
                this.gameName,
                this.mode,
                this.map,
                this.server
        );
        this.recorder.start();
        this.isRecording = true;
        ReplayManager.activeReplays.put(this.id, this);
    }

    public void play(Player watcher) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(Core.getInstance(), () -> this.startReplay(watcher));
        } else {
            this.startReplay(watcher);
        }
    }

    private void startReplay(Player watcher) {
        this.replayer = new Replayer(this, watcher);
        this.replayer.start();
        this.isPlaying = true;
    }

    // --------------------
    // Getters e Setters
    // --------------------

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setRecording(boolean recording) {
        this.isRecording = recording;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public ReplayInfo getReplayInfo() {
        return this.replayInfo;
    }

    public void setReplayInfo(ReplayInfo replayInfo) {
        this.replayInfo = replayInfo;
    }


    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getMap() { return map; }
    public void setMap(String map) { this.map = map; }

    public String getServer() { return server; }
    public void setServer(String server) { this.server = server; }

    public int getPlayers() { return players; }
    public void setPlayers(int players) { this.players = players; }

}

/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReplayAPI {
    private static ReplayAPI instance;
    private final HookManager hookManager = new HookManager();

    private ReplayAPI() {
    }

    public void registerHook(IReplayHook hook) {
        this.hookManager.registerHook(hook);
    }

    public void unregisterHook(IReplayHook hook) {
        this.hookManager.unregisterHook(hook);
    }

    public void recordReplay(String name, Player ... players) {
        ArrayList<Player> toRecord;
        if (players != null && players.length > 0) {
            toRecord = (ArrayList<Player>) Arrays.asList(players);
        } else {
            toRecord = new ArrayList<>(Bukkit.getOnlinePlayers());
        }
        this.recordReplay(name, toRecord);
    }

    public void recordReplay(String name, List<Player> players) {
        Replay replay = new Replay();
        if (name != null) {
            replay.setId(name);
        }
        replay.recordAll(players);
    }

    public void stopReplay(String name, boolean save) {
        this.stopReplay(name, save, false);
    }

    public void stopReplay(String name, boolean save, boolean ignoreEmpty) {
        if (ReplayManager.activeReplays.containsKey(name)) {
            boolean shouldSave;
            Replay replay = ReplayManager.activeReplays.get(name);
            boolean bl = shouldSave = save && (!replay.getRecorder().getData().getActions().isEmpty() || !ignoreEmpty);
            if (replay.isRecording()) {
                replay.getRecorder().stop(shouldSave);
            }
        }
    }

    public void playReplay(String name, final Player watcher) {
        if (ReplaySaver.exists(name) && !ReplayHelper.replaySessions.containsKey(watcher.getName())) {
            ReplaySaver.load(name, new Consumer<Replay>(){

                @Override
                public void accept(Replay replay) {
                    replay.play(watcher);
                }
            });
        }
    }

    public void jumpToReplayTime(Player watcher, Integer second) {
        Replayer replayer;
        if (ReplayHelper.replaySessions.containsKey(watcher.getName()) && (replayer = ReplayHelper.replaySessions.get(watcher.getName())) != null) {
            int duration = replayer.getReplay().getData().getDuration() / 20;
            if (second > 0 && second <= duration) {
                replayer.getUtils().jumpTo(second);
            }
        }
    }

    public void registerReplaySaver(IReplaySaver replaySaver) {
        ReplaySaver.register(replaySaver);
    }

    public HookManager getHookManager() {
        return this.hookManager;
    }

    public static ReplayAPI getInstance() {
        if (instance == null) {
            instance = new ReplayAPI();
        }
        return instance;
    }
}


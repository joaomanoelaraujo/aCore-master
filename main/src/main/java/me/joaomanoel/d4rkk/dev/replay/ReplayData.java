/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ReplayData
implements Serializable {
    private static final long serialVersionUID = -5238528050567979737L;
    private HashMap<Integer, List<ActionData>> actions = new HashMap();
    private HashMap<String, PlayerWatcher> watchers = new HashMap();
    private int duration;
    private List<String> creator;
    private ReplayQuality quality = ConfigManager.QUALITY;

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<String> getCreator() {
        return this.creator;
    }

    public void setCreator(List<String> creator) {
        this.creator = creator;
    }

    public ReplayQuality getQuality() {
        return this.quality;
    }

    public HashMap<Integer, List<ActionData>> getActions() {
        return this.actions;
    }

    public HashMap<String, PlayerWatcher> getWatchers() {
        return this.watchers;
    }

    public void setWatchers(HashMap<String, PlayerWatcher> watchers) {
        this.watchers = watchers;
    }

    public PlayerWatcher getWatcher(String name) {
        if (this.watchers.containsKey(name)) {
            return this.watchers.get(name);
        }
        return null;
    }
}


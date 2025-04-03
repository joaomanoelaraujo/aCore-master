/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.List;

public class ReplayInfo {
    private String id;
    private List<String> creator;
    private Long time;
    private int duration;

    public ReplayInfo(String id, List<String> creator, Long time, int duration) {
        this.id = id;
        this.creator = creator;
        this.time = time;
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public List<String> getCreator() {
        return this.creator;
    }

    public String getID() {
        return this.id;
    }

    public Long getTime() {
        return this.time;
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.io.Serializable;

public class ActionData
implements Serializable {
    private static final long serialVersionUID = 8865556874115905167L;
    private int tickIndex;
    private ActionType type;
    private PacketData packetData;
    private String name;

    public ActionData(int tickIndex, ActionType type, String name, PacketData packetData) {
        this.tickIndex = tickIndex;
        this.type = type;
        this.packetData = packetData;
        this.name = name;
    }

    public int getTickIndex() {
        return this.tickIndex;
    }

    public ActionType getType() {
        return this.type;
    }

    public PacketData getPacketData() {
        return this.packetData;
    }

    public String getName() {
        return this.name;
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class AnimationData
extends PacketData {
    private static final long serialVersionUID = -5227638148471461255L;
    private final int id;

    public AnimationData(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}


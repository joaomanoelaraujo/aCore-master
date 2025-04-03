/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class EntityAnimationData
extends PacketData {
    private static final long serialVersionUID = 3334893591520224930L;
    private int entId;
    private int id;

    public EntityAnimationData(int entId, int id) {
        this.id = id;
        this.entId = entId;
    }

    public int getId() {
        return this.id;
    }

    public int getEntId() {
        return this.entId;
    }
}


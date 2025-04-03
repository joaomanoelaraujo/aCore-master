/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class ItemData
extends PacketData {
    private static final long serialVersionUID = 3882181315164039909L;
    private int id;
    private int subId;
    private SerializableItemStack itemStack;

    public ItemData(int id, int subId) {
        this.id = id;
        this.subId = subId;
    }

    public ItemData(SerializableItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getId() {
        return this.id;
    }

    public int getSubId() {
        return this.subId;
    }

    public SerializableItemStack getItemStack() {
        return this.itemStack;
    }
}


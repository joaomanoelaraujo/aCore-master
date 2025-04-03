/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class EntityItemData
extends PacketData {
    private static final long serialVersionUID = 2309497657075148314L;
    private int action;
    private int id;
    private ItemData itemData;
    private LocationData location;
    private LocationData velocity;

    public EntityItemData(int action, int id, ItemData itemData, LocationData location, LocationData velocity) {
        this.action = action;
        this.itemData = itemData;
        this.location = location;
        this.id = id;
        this.velocity = velocity;
    }

    public int getAction() {
        return this.action;
    }

    public ItemData getItemData() {
        return this.itemData;
    }

    public LocationData getLocation() {
        return this.location;
    }

    public int getId() {
        return this.id;
    }

    public LocationData getVelocity() {
        return this.velocity;
    }
}


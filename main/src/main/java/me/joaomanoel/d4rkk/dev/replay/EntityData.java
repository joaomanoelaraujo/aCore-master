/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class EntityData
extends PacketData {
    private static final long serialVersionUID = 2309497657075148314L;
    private int action;
    private int id;
    private LocationData location;
    private String type;

    public EntityData(int action, int id, LocationData location, String type) {
        this.action = action;
        this.id = id;
        this.location = location;
        this.type = type;
    }

    public int getAction() {
        return this.action;
    }

    public int getId() {
        return this.id;
    }

    public LocationData getLocation() {
        return this.location;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


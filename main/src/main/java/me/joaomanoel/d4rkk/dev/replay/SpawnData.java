/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.UUID;

public class SpawnData
extends PacketData {
    private static final long serialVersionUID = -7896939862437693109L;
    private UUID uuid;
    private LocationData location;
    private SignatureData signature;

    public SpawnData(UUID uuid, LocationData location, SignatureData signature) {
        this.uuid = uuid;
        this.location = location;
        this.signature = signature;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public LocationData getLocation() {
        return this.location;
    }

    public SignatureData getSignature() {
        return this.signature;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}


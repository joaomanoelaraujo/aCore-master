/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class MetadataUpdate
extends PacketData {
    private static final long serialVersionUID = -8969498588009941633L;
    private boolean burning;
    private boolean blocking;
    private boolean gliding;
    private boolean swimming;

    public MetadataUpdate(boolean burning, boolean blocking) {
        this.burning = burning;
        this.blocking = blocking;
    }

    public MetadataUpdate(boolean burning, boolean blocking, boolean gliding) {
        this(burning, blocking);
        this.gliding = gliding;
    }

    public MetadataUpdate(boolean burning, boolean blocking, boolean gliding, boolean swimming) {
        this(burning, blocking, gliding);
        this.swimming = swimming;
    }

    public boolean isBurning() {
        return this.burning;
    }

    public boolean isBlocking() {
        return this.blocking;
    }

    public boolean isGliding() {
        return this.gliding;
    }

    public boolean isSwimming() {
        return this.swimming;
    }

    public static MetadataUpdate fromWatcher(PlayerWatcher watcher) {
        return new MetadataUpdate(watcher.isBurning(), watcher.isBlocking(), watcher.isElytra(), watcher.isSwimming());
    }
}


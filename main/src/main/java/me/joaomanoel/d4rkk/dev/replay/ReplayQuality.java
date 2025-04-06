/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public enum ReplayQuality {
    LOW("§cLow"),
    MEDIUM("§eMedium"),
    HIGH("§aHigh");

    private String qualityName;

    private ReplayQuality(String qualityName) {
        this.qualityName = qualityName;
    }

    public String getQualityName() {
        return this.qualityName;
    }
}


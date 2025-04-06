/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class SignatureData
extends PacketData {
    private static final long serialVersionUID = -5019331850509482609L;
    private String name;
    private String value;
    private String signature;

    public SignatureData(String name, String value, String signature) {
        this.name = name;
        this.signature = signature;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getValue() {
        return this.value;
    }
}


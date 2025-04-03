/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class ChatData
extends PacketData {
    private static final long serialVersionUID = 6849586468365004854L;
    private String message;

    public ChatData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerAction
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.comphenix.protocol.wrappers.EnumWrappers;

public class EntityActionData
extends PacketData {
    private static final long serialVersionUID = 7841723539864388570L;
    private EnumWrappers.PlayerAction action;

    public EntityActionData(EnumWrappers.PlayerAction action) {
        this.action = action;
    }

    public EnumWrappers.PlayerAction getAction() {
        return this.action;
    }
}


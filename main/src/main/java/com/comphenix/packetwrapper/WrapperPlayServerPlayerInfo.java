/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.wrappers.EnumWrappers$PlayerInfoAction
 *  com.comphenix.protocol.wrappers.PlayerInfoData
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import java.util.List;

public class WrapperPlayServerPlayerInfo
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.PLAYER_INFO;

    public WrapperPlayServerPlayerInfo() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerPlayerInfo(PacketContainer packet) {
        super(packet, TYPE);
    }

    public EnumWrappers.PlayerInfoAction getAction() {
        return (EnumWrappers.PlayerInfoAction)this.handle.getPlayerInfoAction().read(0);
    }

    public void setAction(EnumWrappers.PlayerInfoAction value) {
        this.handle.getPlayerInfoAction().write(0, value);
    }

    public List<PlayerInfoData> getData() {
        return (List)this.handle.getPlayerInfoDataLists().read(0);
    }

    public void setData(List<PlayerInfoData> value) {
        this.handle.getPlayerInfoDataLists().write(0, value);
    }
}


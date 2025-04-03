/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.reflect.IntEnum
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.IntEnum;
import java.util.Collection;
import java.util.List;

public class WrapperPlayServerScoreboardTeam
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_TEAM;

    public WrapperPlayServerScoreboardTeam() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerScoreboardTeam(PacketContainer packet) {
        super(packet, TYPE);
    }

    public String getName() {
        return (String)this.handle.getStrings().read(0);
    }

    public void setName(String value) {
        this.handle.getStrings().write(0, value);
    }

    public String getDisplayName() {
        return (String)this.handle.getStrings().read(1);
    }

    public void setDisplayName(String value) {
        this.handle.getStrings().write(1, value);
    }

    public String getPrefix() {
        return (String)this.handle.getStrings().read(2);
    }

    public void setPrefix(String value) {
        this.handle.getStrings().write(2, value);
    }

    public String getSuffix() {
        return (String)this.handle.getStrings().read(3);
    }

    public void setSuffix(String value) {
        this.handle.getStrings().write(3, value);
    }

    public String getNameTagVisibility() {
        return (String)this.handle.getStrings().read(4);
    }

    public void setNameTagVisibility(String value) {
        this.handle.getStrings().write(4, value);
    }

    public int getColor() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public void setColor(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public String getCollisionRule() {
        return (String)this.handle.getStrings().read(5);
    }

    public void setCollisionRule(String value) {
        this.handle.getStrings().write(5, value);
    }

    public List<String> getPlayers() {
        return (List)this.handle.getSpecificModifier(Collection.class).read(0);
    }

    public void setPlayers(List<String> value) {
        this.handle.getSpecificModifier(Collection.class).write(0, value);
    }

    public int getMode() {
        return (Integer)this.handle.getIntegers().read(1);
    }

    public void setMode(int value) {
        this.handle.getIntegers().write(1, value);
    }

    public int getPackOptionData() {
        return (Integer)this.handle.getIntegers().read(2);
    }

    public void setPackOptionData(int value) {
        this.handle.getIntegers().write(2, value);
    }

    public static class Mode
    extends IntEnum {
        public static final int TEAM_CREATED = 0;
        public static final int TEAM_REMOVED = 1;
        public static final int TEAM_UPDATED = 2;
        public static final int PLAYERS_ADDED = 3;
        public static final int PLAYERS_REMOVED = 4;
        private static final Mode INSTANCE = new Mode();

        public static Mode getInstance() {
            return INSTANCE;
        }
    }
}


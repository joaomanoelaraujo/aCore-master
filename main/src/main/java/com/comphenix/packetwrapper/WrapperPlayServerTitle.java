/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.wrappers.EnumWrappers$TitleAction
 *  com.comphenix.protocol.wrappers.WrappedChatComponent
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperPlayServerTitle
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.TITLE;

    public WrapperPlayServerTitle() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerTitle(PacketContainer packet) {
        super(packet, TYPE);
    }

    public EnumWrappers.TitleAction getAction() {
        return (EnumWrappers.TitleAction)this.handle.getTitleActions().read(0);
    }

    public void setAction(EnumWrappers.TitleAction value) {
        this.handle.getTitleActions().write(0, value);
    }

    public WrappedChatComponent getTitle() {
        return (WrappedChatComponent)this.handle.getChatComponents().read(0);
    }

    public void setTitle(WrappedChatComponent value) {
        this.handle.getChatComponents().write(0, value);
    }

    public int getFadeIn() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public void setFadeIn(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public int getStay() {
        return (Integer)this.handle.getIntegers().read(1);
    }

    public void setStay(int value) {
        this.handle.getIntegers().write(1, value);
    }

    public int getFadeOut() {
        return (Integer)this.handle.getIntegers().read(2);
    }

    public void setFadeOut(int value) {
        this.handle.getIntegers().write(2, value);
    }
}


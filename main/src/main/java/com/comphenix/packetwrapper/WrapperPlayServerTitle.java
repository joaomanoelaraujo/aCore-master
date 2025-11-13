package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import java.lang.reflect.Field;

public class WrapperPlayServerTitle extends AbstractPacket {

    private static final PacketType TYPE = getPacketType();

    private static PacketType getPacketType() {
        try {
            Field field = PacketType.Play.Server.class.getField("TITLE");
            return (PacketType) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            try {
                Field field = PacketType.Play.Server.class.getField("SET_TITLE_TEXT");
                return (PacketType) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new RuntimeException("Unable to find title packet type", ex);
            }
        }
    }

    public WrapperPlayServerTitle() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerTitle(PacketContainer packet) {
        super(packet, TYPE);
    }

    public EnumWrappers.TitleAction getAction() {
        try {
            return (EnumWrappers.TitleAction) this.handle.getTitleActions().read(0);
        } catch (Exception e) {
            return null;
        }
    }

    public void setAction(EnumWrappers.TitleAction value) {
        try {
            this.handle.getTitleActions().write(0, value);
        } catch (Exception e) {
        }
    }

    public WrappedChatComponent getTitle() {
        try {
            return (WrappedChatComponent) this.handle.getChatComponents().read(0);
        } catch (Exception e) {
            return null;
        }
    }

    public void setTitle(WrappedChatComponent value) {
        try {
            this.handle.getChatComponents().write(0, value);
        } catch (Exception e) {
        }
    }

    public int getFadeIn() {
        try {
            return (Integer) this.handle.getIntegers().read(0);
        } catch (Exception e) {
            return 10;
        }
    }

    public void setFadeIn(int value) {
        try {
            this.handle.getIntegers().write(0, value);
        } catch (Exception e) {
        }
    }

    public int getStay() {
        try {
            return (Integer) this.handle.getIntegers().read(1);
        } catch (Exception e) {
            return 70;
        }
    }

    public void setStay(int value) {
        try {
            this.handle.getIntegers().write(1, value);
        } catch (Exception e) {
        }
    }

    public int getFadeOut() {
        try {
            return (Integer) this.handle.getIntegers().read(2);
        } catch (Exception e) {
            return 20;
        }
    }

    public void setFadeOut(int value) {
        try {
            this.handle.getIntegers().write(2, value);
        } catch (Exception e) {
        }
    }
}

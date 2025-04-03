/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.PacketContainer
 *  org.bukkit.inventory.ItemStack
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.ItemStack;

public class WrapperPlayClientWindowClick
extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Client.WINDOW_CLICK;

    public WrapperPlayClientWindowClick() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientWindowClick(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getWindowId() {
        return (Integer)this.handle.getIntegers().read(0);
    }

    public void setWindowId(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public int getSlot() {
        return (Integer)this.handle.getIntegers().read(1);
    }

    public void setSlot(int value) {
        this.handle.getIntegers().write(1, value);
    }

    public int getButton() {
        return (Integer)this.handle.getIntegers().read(2);
    }

    public void setButton(int value) {
        this.handle.getIntegers().write(2, value);
    }

    public short getActionNumber() {
        return (Short)this.handle.getShorts().read(0);
    }

    public void setActionNumber(short value) {
        this.handle.getShorts().write(0, value);
    }

    public ItemStack getClickedItem() {
        return (ItemStack)this.handle.getItemModifier().read(0);
    }

    public void setClickedItem(ItemStack value) {
        this.handle.getItemModifier().write(0, value);
    }

    public InventoryClickType getShift() {
        return (InventoryClickType)(this.handle.getEnumModifier(InventoryClickType.class, 5).read(0));
    }

    public void setShift(InventoryClickType value) {
        this.handle.getEnumModifier(InventoryClickType.class, 5).write(0, value);
    }

    public static enum InventoryClickType {
        PICKUP,
        QUICK_MOVE,
        SWAP,
        CLONE,
        THROW,
        QUICK_CRAFT,
        PICKUP_ALL;

    }
}


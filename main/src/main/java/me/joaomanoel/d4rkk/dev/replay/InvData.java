/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class InvData
extends PacketData {
    private static final long serialVersionUID = -5055461506908394060L;
    private ItemData head;
    private ItemData chest;
    private ItemData leg;
    private ItemData boots;
    private ItemData mainHand;
    private ItemData offHand;

    public InvData(ItemData head, ItemData chest, ItemData leg, ItemData boots, ItemData mainHand, ItemData offHand) {
        this.head = head;
        this.chest = chest;
        this.leg = leg;
        this.boots = boots;
        this.mainHand = mainHand;
        this.offHand = offHand;
    }

    public InvData() {
    }

    public ItemData getBoots() {
        return this.boots;
    }

    public ItemData getChest() {
        return this.chest;
    }

    public ItemData getHead() {
        return this.head;
    }

    public ItemData getLeg() {
        return this.leg;
    }

    public ItemData getMainHand() {
        return this.mainHand;
    }

    public ItemData getOffHand() {
        return this.offHand;
    }

    public void setBoots(ItemData boots) {
        this.boots = boots;
    }

    public void setChest(ItemData chest) {
        this.chest = chest;
    }

    public void setHead(ItemData head) {
        this.head = head;
    }

    public void setLeg(ItemData leg) {
        this.leg = leg;
    }

    public void setMainHand(ItemData mainHand) {
        this.mainHand = mainHand;
    }

    public void setOffHand(ItemData offHand) {
        this.offHand = offHand;
    }
}


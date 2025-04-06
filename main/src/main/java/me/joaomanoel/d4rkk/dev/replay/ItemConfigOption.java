/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.Material;

public class ItemConfigOption {
    private Material material;
    private String name;
    private int slot;
    private String owner;
    private int data;
    private boolean enabled;

    public ItemConfigOption(Material material, String name, int slot) {
        this.material = material;
        this.name = name;
        this.slot = slot;
        this.enabled = true;
    }

    public ItemConfigOption(Material material, String name, int slot, String owner, int data) {
        this(material, name, slot);
        this.data = data;
        this.owner = owner;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getName() {
        return this.name;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return this.data;
    }

    public ItemConfigOption enable(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}


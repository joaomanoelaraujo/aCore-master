/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 */
package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.Material;
import org.bukkit.block.Block;

public enum MaterialBridge {
    WATCH("CLOCK"),
    WOOD_DOOR("WOODEN_DOOR");

    private String materialName;

    private MaterialBridge(String materialName) {
        this.materialName = materialName;
    }

    public Material toMaterial() {
        return Material.valueOf((String)this.toString());
    }

    public String getMaterialName() {
        return this.materialName;
    }

    public static Material fromID(int id) {
        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_13) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_14) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_15) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_16) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_17) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_18) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_19)) {
            for (Material mat : Material.values()) {
                if (mat.getId() != id) continue;
                return mat;
            }
            return null;
        }
        return Material.getMaterial((int)id);
    }

    public static Material getWithoutLegacy(String material) {
        try {
            Object enumField = ReflectionHelper.getInstance().matchMaterial(material, false);
            return (Material)enumField;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Material getBlockDataMaterial(Block block) {
        try {
            Object blockData = ReflectionHelper.getInstance().getBlockData(block);
            Object materialField = ReflectionHelper.getInstance().getBlockDataMaterial(blockData);
            return (Material)materialField;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


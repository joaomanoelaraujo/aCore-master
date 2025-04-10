/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemConfig {
    public static File file = new File(Core.getInstance().getDataFolder(), "items.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration((File)file);
    public static HashMap<ItemConfigType, ItemConfigOption> items = new HashMap();

    public static void loadConfig() {
        ItemConfig.addDefaults();
        if (!file.exists()) {
            for (ItemConfigType type : items.keySet()) {
                String name = type.name().toLowerCase();
                ItemConfigOption item = items.get((Object)type);
                cfg.set("items." + name + ".name", (Object)item.getName());
                cfg.set("items." + name + ".id", (Object)(item.getMaterial().name() + (item.getData() != 0 ? ":" + item.getData() : "")));
                cfg.set("items." + name + ".slot", (Object)item.getSlot());
                if (item.getOwner() != null) {
                    cfg.set("items." + name + ".owner", (Object)item.getOwner());
                }
                cfg.set("items." + name + ".enabled", (Object)item.isEnabled());
            }
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadData() {
        for (ItemConfigType type : ItemConfigType.values()) {
            String name = type.name().toLowerCase();
            String displayName = cfg.getString("items." + name + ".name");
            String owner = cfg.getString("items." + name + ".owner");
            String matString = cfg.getString("items." + name + ".id").toUpperCase();
            int slot = cfg.getInt("items." + name + ".slot");
            boolean enabled = cfg.getBoolean("items." + name + ".enabled", true);
            int data = 0;
            if (matString.contains(":")) {
                String[] split = matString.split(":");
                matString = split[0];
                data = Integer.valueOf(split[1]);
            }
            Material material = Material.valueOf((String)matString);
            items.put(type, new ItemConfigOption(material, displayName, slot, owner, data).enable(enabled));
        }
    }

    public static ItemConfigOption getItem(ItemConfigType type) {
        return items.get((Object)type);
    }

    public static ItemConfigType getByIdAndName(Material material, String name) {
        for (ItemConfigType type : items.keySet()) {
            ItemConfigOption option = items.get((Object)type);
            if (option.getMaterial() == Material.WOOD_DOOR && (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_13) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_14) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_15) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_16) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_17) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_18) || VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_19)) && material.name().equals(MaterialBridge.WOOD_DOOR.getMaterialName()) && option.getName().equals(name)) {
                return type;
            }
            if (option.getMaterial() != material || !option.getName().equals(name)) continue;
            return type;
        }
        return null;
    }

    private static void addDefaults() {
        items.put(ItemConfigType.TELEPORT, new ItemConfigOption(Material.COMPASS, "&7Teleport", 0));
        items.put(ItemConfigType.SPEED, new ItemConfigOption(Material.WATCH, "&cSlow &8[&eRight&8] &aFast &8[&eShift Right&8]", 1));
        items.put(ItemConfigType.LEAVE, new ItemConfigOption(Material.WOOD_DOOR, "&7Leave replay", 8));
        items.put(ItemConfigType.FORWARD, new ItemConfigOption(Material.SKULL_ITEM, "&a\u00bb &e10 seconds", 5, "MHF_ArrowRight", 3));
        items.put(ItemConfigType.BACKWARD, new ItemConfigOption(Material.SKULL_ITEM, "&c\u00ab &e10 seconds", 3, "MHF_ArrowLeft", 3));
        items.put(ItemConfigType.RESUME, new ItemConfigOption(Material.SLIME_BLOCK, "&aResume", 4));
        items.put(ItemConfigType.PAUSE, new ItemConfigOption(Material.SKULL_ITEM, "&cPause", 4, "Push_red_button", 3));
    }
}


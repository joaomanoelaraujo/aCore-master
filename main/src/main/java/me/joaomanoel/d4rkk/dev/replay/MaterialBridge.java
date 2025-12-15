package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.Material;
import org.bukkit.block.Block;

public enum MaterialBridge {

    // Itens antigos renomeados nas novas versões
    WATCH("WATCH", "CLOCK"),
    WOOD_DOOR("WOOD_DOOR", "OAK_DOOR"),
    IRON_DOOR("IRON_DOOR", "IRON_DOOR"),
    SKULL_ITEM("SKULL_ITEM", "PLAYER_HEAD"),
    GOLD_HELMET("GOLD_HELMET", "GOLDEN_HELMET"),
    GOLD_CHESTPLATE("GOLD_CHESTPLATE", "GOLDEN_CHESTPLATE"),
    GOLD_LEGGINGS("GOLD_LEGGINGS", "GOLDEN_LEGGINGS"),
    GOLD_BOOTS("GOLD_BOOTS", "GOLDEN_BOOTS"),
    WOOD_BUTTON("WOOD_BUTTON", "OAK_BUTTON"),
    WOOD_PLATE("WOOD_PLATE", "OAK_PRESSURE_PLATE"),
    WOOD_AXE("WOOD_AXE", "WOODEN_AXE"),
    WOOD_PICKAXE("WOOD_PICKAXE", "WOODEN_PICKAXE"),
    WOOD_SWORD("WOOD_SWORD", "WOODEN_SWORD"),
    WOOD_HOE("WOOD_HOE", "WOODEN_HOE"),
    WOOD_SPADE("WOOD_SPADE", "WOODEN_SHOVEL"),
    STONE_SPADE("STONE_SPADE", "STONE_SHOVEL"),
    IRON_SPADE("IRON_SPADE", "IRON_SHOVEL"),
    GOLD_SPADE("GOLD_SPADE", "GOLDEN_SHOVEL"),
    STONE_PLATE("STONE_PLATE", "STONE_PRESSURE_PLATE"),
    STEP("STEP", "STONE_SLAB"),
    DOUBLE_STEP("DOUBLE_STEP", "DOUBLE_STONE_SLAB"),
    NETHER_FENCE("NETHER_FENCE", "NETHER_BRICK_FENCE"),
    BREWING_STAND_ITEM("BREWING_STAND_ITEM", "BREWING_STAND"),
    CAULDRON_ITEM("CAULDRON_ITEM", "CAULDRON"),
    REDSTONE_TORCH_ON("REDSTONE_TORCH_ON", "REDSTONE_TORCH"),
    SIGN("SIGN", "OAK_SIGN"),
    SIGN_POST("SIGN_POST", "OAK_SIGN"),
    WALL_SIGN("WALL_SIGN", "OAK_WALL_SIGN"),
    MINECART("MINECART", "MINECART"),
    SLIME_BLOCK("SLIME_BLOCK", "SLIME_BLOCK");

    private final String legacyName;
    private final String modernName;

    MaterialBridge(String legacyName, String modernName) {
        this.legacyName = legacyName;
        this.modernName = modernName;
    }

    public String getMaterialName() {
        return VersionUtil.isLegacy() ? legacyName : modernName;
    }

    public Material toMaterial() {
        try {
            return Material.valueOf(getMaterialName());
        } catch (IllegalArgumentException e) {
            try {
                return Material.valueOf(legacyName);
            } catch (IllegalArgumentException ex) {
                try {
                    return Material.valueOf(modernName);
                } catch (IllegalArgumentException ex2) {
                    return Material.STONE;
                }
            }
        }
    }

    public static Material fromName(String name) {
        if (name == null) return Material.STONE;
        name = name.toUpperCase();

        // Conversões conhecidas antigas -> novas
        switch (name) {
            case "WATCH": name = "CLOCK"; break;
            case "WOOD_DOOR": name = "OAK_DOOR"; break;
            case "WOOD_BUTTON": name = "OAK_BUTTON"; break;
            case "WOOD_PLATE": name = "OAK_PRESSURE_PLATE"; break;
            case "WOOD_SWORD": name = "WOODEN_SWORD"; break;
            case "WOOD_AXE": name = "WOODEN_AXE"; break;
            case "WOOD_HOE": name = "WOODEN_HOE"; break;
            case "WOOD_PICKAXE": name = "WOODEN_PICKAXE"; break;
            case "WOOD_SPADE": name = "WOODEN_SHOVEL"; break;
            case "GOLD_HELMET": name = "GOLDEN_HELMET"; break;
            case "GOLD_BOOTS": name = "GOLDEN_BOOTS"; break;
            case "GOLD_CHESTPLATE": name = "GOLDEN_CHESTPLATE"; break;
            case "GOLD_LEGGINGS": name = "GOLDEN_LEGGINGS"; break;
            case "SKULL_ITEM": name = "PLAYER_HEAD"; break;
            case "STEP": name = "STONE_SLAB"; break;
            case "DOUBLE_STEP": name = "DOUBLE_STONE_SLAB"; break;
            case "NETHER_FENCE": name = "NETHER_BRICK_FENCE"; break;
            case "SIGN": case "SIGN_POST": name = "OAK_SIGN"; break;
            case "WALL_SIGN": name = "OAK_WALL_SIGN"; break;
        }

        try {
            return Material.valueOf(name);
        } catch (IllegalArgumentException e) {
            // fallback: tenta o nome antigo
            try {
                return Material.valueOf(name.replace("OAK_", "WOOD_").replace("GOLDEN_", "GOLD_"));
            } catch (Exception ex) {
                return Material.STONE;
            }
        }
    }

    public static Material fromID(int id) {
        try {
            if (VersionUtil.isLegacy()) {
                return Material.getMaterial(id);
            }
            for (Material mat : Material.values()) {
                try {
                    if (mat.getId() == id) return mat;
                } catch (Throwable ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Material getBlockDataMaterial(Block block) {
        try {
            Object blockData = ReflectionHelper.getInstance().getBlockData(block);
            Object materialField = ReflectionHelper.getInstance().getBlockDataMaterial(blockData);
            return (Material) materialField;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

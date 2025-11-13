package me.joaomanoel.d4rkk.dev.utils;

import org.bukkit.Material;

public class MaterialBridge {

    public static Material get(String legacyName) {
        legacyName = legacyName.toUpperCase();

        // 🔹 Correções automáticas
        switch (legacyName) {
            case "WATCH":
                legacyName = "CLOCK";
                break;
            case "WOOD_DOOR":
                legacyName = "OAK_DOOR";
                break;
            case "SKULL_ITEM":
                legacyName = "PLAYER_HEAD";
                break;
            case "STONE_PLATE":
                legacyName = "STONE_PRESSURE_PLATE";
                break;
            case "WORKBENCH":
                legacyName = "CRAFTING_TABLE";
                break;
            case "IRON_FENCE":
                legacyName = "IRON_BARS";
                break;
            case "REDSTONE_TORCH_ON":
                legacyName = "REDSTONE_TORCH";
                break;
        }

        try {
            return Material.valueOf(legacyName);
        } catch (IllegalArgumentException e) {
            // fallback se o nome ainda estiver errado
            return Material.STONE;
        }
    }
}

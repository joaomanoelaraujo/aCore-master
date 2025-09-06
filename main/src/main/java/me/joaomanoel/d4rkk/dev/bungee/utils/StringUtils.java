package me.joaomanoel.d4rkk.dev.bungee.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class StringUtils {

    public static String formatColors(String text) {
        if (text == null) return null;
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String deformatColors(String text) {
        if (text == null) return null;
        return ChatColor.stripColor(text);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static String capitalize(String text) {
        if (isEmpty(text)) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static String join(List<String> list, String separator) {
        if (list == null || list.isEmpty()) return "";
        return String.join(separator, list);
    }
}
package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.Language;

/**
 * VERSÃO BUKKIT - Usa apenas Language (sem LanguageBungee)
 */
public class LanguageUtils {

    public static String get(String key) {
        try {
            // No Bukkit, sempre usa Language
            String nativeName = key.replace("$", "_");
            return (String) Language.class.getField(nativeName).get(null);
        } catch (Exception e) {
            return "Missing lang: " + key;
        }
    }
}
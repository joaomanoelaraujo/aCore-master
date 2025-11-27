package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Language;
import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;

public class LanguageUtils {

    public static String get(String key) {
        boolean bungee = Core.getInstance().isBungeeEnabled();

        try {
            if (bungee) {
                return (String) LanguageBungee.class.getField(key).get(null);
            } else {
                return (String) Language.class.getField(key).get(null);
            }
        } catch (Exception e) {
            return "Missing lang: " + key;
        }
    }
}

/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.ChatColor;

public class MessageBuilder {
    private String message;

    public MessageBuilder(String message) {
        this.message = message;
    }

    public MessageBuilder set(String key, String value) {
        if (message != null && value != null) {
            message = message.replace("{" + key + "}", value);
            message = message.replace("%" + key + "%", value);
        }
        return this;
    }

    public MessageBuilder set(String key, int value) {
        return set(key, String.valueOf(value));
    }

    public MessageBuilder set(String key, double value) {
        return set(key, String.valueOf(value));
    }

    public MessageBuilder set(String key, boolean value) {
        return set(key, String.valueOf(value));
    }

    public String build() {
        return message != null ? message.replace("&", "§") : "";
    }

    public static String format(String message, Object... replacements) {
        if (message == null) return "";

        String result = message;
        for (int i = 0; i < replacements.length - 1; i += 2) {
            String key = String.valueOf(replacements[i]);
            String value = String.valueOf(replacements[i + 1]);
            result = result.replace("{" + key + "}", value);
            result = result.replace("%" + key + "%", value);
        }

        return result.replace("&", "§");
    }
}


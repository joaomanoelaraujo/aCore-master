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

    public MessageBuilder set(String key, Object value) {
        if (this.message != null && this.message.contains("{" + key + "}")) {
            this.message = this.message.replace("{" + key + "}", value.toString());
        }
        return this;
    }

    public String build() {
        return this.message != null && this.message.length() > 0 ? ChatColor.translateAlternateColorCodes((char)'&', (String)this.message) : null;
    }
}


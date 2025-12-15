/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

public class LogUtils {
    public static void log(String message) {
        Core.getInstance().getLogger().info(message);
    }
}


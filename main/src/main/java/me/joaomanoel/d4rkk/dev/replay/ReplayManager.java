/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.replay.ConfigManager;
import me.joaomanoel.d4rkk.dev.replay.Replay;
import me.joaomanoel.d4rkk.dev.replay.ReplayAPI;
import me.joaomanoel.d4rkk.dev.replay.ReplayListener;
import java.util.HashMap;
import org.bukkit.entity.Player;

public class ReplayManager {
    public static HashMap<String, Replay> activeReplays = new HashMap<>();

    public static void register() {
        ReplayManager.registerEvents();
        if (ConfigManager.RECORD_STARTUP) {
            ReplayAPI.getInstance().recordReplay(null);
        }
    }

    private static void registerEvents() {
        new ReplayListener().register();
    }

    private static void registerCommands() {
    }
}


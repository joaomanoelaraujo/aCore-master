/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.replay.ConfigManager;
import me.joaomanoel.d4rkk.dev.replay.DatabaseReplaySaver;
import me.joaomanoel.d4rkk.dev.replay.DefaultReplaySaver;
import me.joaomanoel.d4rkk.dev.replay.LogUtils;
import me.joaomanoel.d4rkk.dev.replay.ReplaySaver;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ReplayCleanup {
    public static void cleanupReplays() {
        List<String> replays = ReplaySaver.getReplays();
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)Core.getInstance(), () -> replays.forEach(ReplayCleanup::checkAndDelete));
    }

    private static void checkAndDelete(String replay) {
        LocalDate threshold;
        LocalDate creationdDate = ReplayCleanup.getCreationDate(replay);
        if (creationdDate.isBefore(LocalDate.now().minusDays(ConfigManager.CLEANUP_REPLAYS))) {
            LogUtils.log("Replay " + replay + " has expired. Removing it...");
            ReplaySaver.delete(replay);
        }
    }

    private static LocalDate getCreationDate(String replay) {
        if (ReplaySaver.replaySaver instanceof DefaultReplaySaver) {
            return ReplayCleanup.fromMillis(new File(DefaultReplaySaver.DIR, replay + ".replay").lastModified());
        }
        if (ReplaySaver.replaySaver instanceof DatabaseReplaySaver) {
            return ReplayCleanup.fromMillis(DatabaseReplaySaver.replayCache.get(replay).getTime());
        }
        return LocalDate.now();
    }

    private static LocalDate fromMillis(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}


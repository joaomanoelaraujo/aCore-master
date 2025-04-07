package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.Core;
import org.bukkit.Bukkit;

public class TPSUtil {
    private static final long[] times = new long[3]; // 1s, 5s, 15s
    private static long lastPoll = 0;

    static {
        lastPoll = System.nanoTime();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), () -> {
            long now = System.nanoTime();
            long diff = now - lastPoll;

            System.arraycopy(times, 0, times, 1, times.length - 1);
            times[0] = diff;

            lastPoll = now;
        }, 100L, 1L);
    }

    public static double[] getRecentTPS() {
        double[] tps = new double[3];

        for (int i = 0; i < tps.length; i++) {
            long avgTime = 0;
            for (int j = 0; j <= i; j++) {
                avgTime += times[j];
            }

            avgTime /= (i + 1);
            tps[i] = Math.min(20.0, 1 / (avgTime / 1.0E9));
        }

        return tps;
    }

    public static String formatTPS(double tps) {
        return (tps > 18.0 ? "§a" : tps > 16.0 ? "§e" : "§c") + String.format("%.1f", Math.min(20.0, tps));
    }
}

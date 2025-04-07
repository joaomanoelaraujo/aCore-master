package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.Core;
import org.bukkit.Bukkit;

public class TPSUtil {
    private static final long[] times = new long[3]; // 1s, 5s, 15s
    private static long lastPoll = 0;

    static {
        lastPoll = System.nanoTime();

        // Inicia o monitoramento de TPS
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), () -> {
            long now = System.nanoTime();
            long diff = now - lastPoll;

            // Atualiza os valores
            System.arraycopy(times, 0, times, 1, times.length - 1);
            times[0] = diff;

            lastPoll = now;
        }, 100L, 1L);
    }

    // Calcula o TPS médio em 1s, 5s e 15s
    public static double[] getRecentTPS() {
        double[] tps = new double[3];

        for (int i = 0; i < tps.length; i++) {
            long avgTime = 0;
            // Calcula a média dos tempos, levando em consideração o índice
            for (int j = 0; j <= i; j++) {
                avgTime += times[j];
            }

            // Divide o tempo médio pelo número de elementos (1 para 1s, 3 para 15s, etc.)
            avgTime /= (i + 1);
            // Convertendo o tempo médio em segundos e calculando o TPS
            tps[i] = Math.min(20.0, 1 / (avgTime / 1.0E9));
        }

        return tps;
    }

    // Formata o TPS para exibição
    public static String formatTPS(double tps) {
        return (tps > 18.0 ? "§a" : tps > 16.0 ? "§e" : "§c") + String.format("%.1f", Math.min(20.0, tps));
    }
}

package me.joaomanoel.d4rkk.dev.utils;

import org.bukkit.Bukkit;

/**
 * VersionAPI — identifica a versão do SERVIDOR (Minecraft/Bukkit) em runtime.
 *
 * Uso:
 *   // pega string completa: "1.20.6"
 *   String ver = VersionAPI.getVersion();
 *
 *   // compara diretamente
 *   if (VersionAPI.is("1.8.8")) {
 *     // lógica para servidor 1.8.8
 *   } else if (VersionAPI.is("1.20.6")) {
 *     // lógica para servidor 1.20.6
 *   }
 *
 *   // métodos auxiliares
 *   if (VersionAPI.is1_8_8()) { ... }
 *   if (VersionAPI.is1_20_6()) { ... }
 */
public final class VersionAPI {

    private static final String versionString;
    private static final int major;
    private static final int minor;
    private static final int patch;

    static {
        // getBukkitVersion() geralmente retorna "1.20.6-R0.1-SNAPSHOT"
        // split no '-' para isolar "1.20.6"
        String raw = Bukkit.getBukkitVersion().split("-")[0];
        versionString = raw;

        int maj = 0, min = 0, pat = 0;
        String[] parts = raw.split("\\.");
        try {
            if (parts.length > 0) maj = Integer.parseInt(parts[0]);
            if (parts.length > 1) min = Integer.parseInt(parts[1]);
            if (parts.length > 2) pat = Integer.parseInt(parts[2]);
        } catch (NumberFormatException ignored) {}

        major = maj;
        minor = min;
        patch = pat;
    }

    private VersionAPI() { /* static methods only */ }

    /** Retorna "1.20.6", "1.8.8", etc. */
    public static String getVersion() {
        return versionString;
    }

    public static int getMajor() { return major; }
    public static int getMinor() { return minor; }
    public static int getPatch() { return patch; }

    /** Compara exatamente com a string "X.Y.Z" */
    public static boolean is(String version) {
        return versionString.equals(version);
    }

    /** Checa se é 1.8.8 */
    public static boolean is1_8_8() {
        return major == 1 && minor == 8 && patch == 8;
    }

    /** Checa se é 1.20.6 */
    public static boolean is1_20_6() {
        return major == 1 && minor == 20 && patch == 6;
    }

    /**
     * Compara lexicograficamente:
     * retorna true se a versão do servidor for >= X.Y.Z
     */
    public static boolean atLeast(int maj, int min, int pat) {
        if (major != maj) return major > maj;
        if (minor != min) return minor > min;
        return patch >= pat;
    }

    /**
     * Compara lexicograficamente:
     * retorna true se a versão do servidor for <= X.Y.Z
     */
    public static boolean atMost(int maj, int min, int pat) {
        if (major != maj) return major < maj;
        if (minor != min) return minor < min;
        return patch <= pat;
    }
}

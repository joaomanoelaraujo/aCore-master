/*
 * Atualizado por ChatGPT — Compatível até 1.21+
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VersionUtil {

    public static String VERSION;
    public static String CLEAN_VERSION;

    static {
        String bpName = Bukkit.getServer().getClass().getPackage().getName();
        VERSION = bpName.substring(bpName.lastIndexOf(".") + 1);

        // tenta extrair "v1_20_R3" → "1_20"
        if (VERSION.startsWith("v")) {
            CLEAN_VERSION = VERSION.substring(1, VERSION.length() - 3);
        } else {
            CLEAN_VERSION = VERSION;
        }
    }

    // 🔹 Verifica se a versão atual contém um trecho (ex: V1_20)
    public static boolean isCompatible(VersionEnum ve) {
        return VERSION.toLowerCase().contains(ve.toString().toLowerCase());
    }

    // 🔹 Verifica se está acima ou igual a uma versão
    public static boolean isAbove(VersionEnum ve) {
        VersionEnum current = VersionEnum.getByCleanVersion(CLEAN_VERSION);
        return current != null && current.getOrder() >= ve.getOrder();
    }

    // 🔹 Verifica se está abaixo ou igual a uma versão
    public static boolean isBelow(VersionEnum ve) {
        VersionEnum current = VersionEnum.getByCleanVersion(CLEAN_VERSION);
        return current != null && current.getOrder() <= ve.getOrder();
    }

    // 🔹 Verifica se está entre duas versões
    public static boolean isBetween(VersionEnum ve1, VersionEnum ve2) {
        return isAbove(ve1) && isBelow(ve2);
    }

    // 🔹 Detecta se é uma versão “antiga” (1.8–1.12)
    public static boolean isLegacy() {
        return isBelow(VersionEnum.V1_12);
    }

    public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + VERSION + "." + nmsClassName);
    }

    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Field playerConnectionField = nmsPlayer.getClass().getField("playerConnection");
            Object pConnection = playerConnectionField.get(nmsPlayer);
            pConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(pConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum VersionEnum {
        V1_8(1),
        V1_9(2),
        V1_10(3),
        V1_11(4),
        V1_12(5),
        V1_13(6),
        V1_14(7),
        V1_15(8),
        V1_16(9),
        V1_17(10),
        V1_18(11),
        V1_19(12),
        V1_20(13),
        V1_21(14); // 🔹 já deixa pronto para futuras versões

        private final int order;

        VersionEnum(int order) {
            this.order = order;
        }

        public int getOrder() {
            return this.order;
        }

        public static VersionEnum getByCleanVersion(String clean) {
            if (clean == null) return null;
            clean = clean.toUpperCase().replace(".", "_");
            for (VersionEnum v : values()) {
                if (v.name().contains(clean)) return v;
            }
            // tenta pegar prefixo parcial (ex: 1_20_6 → 1_20)
            if (clean.startsWith("1_20")) return V1_20;
            if (clean.startsWith("1_21")) return V1_21;
            return null;
        }
    }
}

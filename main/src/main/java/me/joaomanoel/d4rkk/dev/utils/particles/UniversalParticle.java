package me.joaomanoel.d4rkk.dev.utils.particles;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

public final class UniversalParticle {

    private static final boolean USE_API;
    private static final Method SPAWN_PARTICLE;
    private static final Class<?> PARTICLE_CLASS;

    static {
        boolean useApi = false;
        Method spawn = null;
        Class<?> pClass = null;
        try {
            pClass = Class.forName("org.bukkit.Particle");
            spawn = Class.forName("org.bukkit.World")
                    .getMethod("spawnParticle",
                               pClass,
                               Location.class,
                               int.class,
                               double.class, double.class, double.class,
                               double.class);
            useApi = true;
        } catch (Exception ex) {
            useApi = false;
        }
        USE_API = useApi;
        SPAWN_PARTICLE = spawn;
        PARTICLE_CLASS = pClass;
    }

    /**
     * Spawna a partícula especificada para o player,
     * compatível com 1.8.8 e com todas as 1.9+.
     */
    public static void spawn(Player player,
                             EnumWrappers.Particle particle,
                             Location loc,
                             int count,
                             double offsetX,
                             double offsetY,
                             double offsetZ,
                             double extra) {
        if (USE_API) {
            try {
                // chama world.spawnParticle(Particle, loc, count, offsets..., extra)
                SPAWN_PARTICLE.invoke(
                    player.getWorld(),
                    PARTICLE_CLASS.cast(particle),
                    loc,
                    count,
                    offsetX, offsetY, offsetZ,
                    extra
                );
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else {
            spawnLegacy(player,
                        particle.name(),
                        loc,
                        count,
                        offsetX, offsetY, offsetZ,
                        extra);
        }
    }

    // fallback NMS 1.8.8
    @SuppressWarnings("unchecked")
    private static void spawnLegacy(Player player,
                                    String bukkitName,
                                    Location loc,
                                    int count,
                                    double dx, double dy, double dz,
                                    double speed) {
        try {
            String version = Bukkit.getServer()
                                   .getClass()
                                   .getPackage()
                                   .getName()
                                   .split("\\.")[3];

            Class<?> packetClass = Class.forName(
                "net.minecraft.server." + version + ".PacketPlayOutWorldParticles"
            );
            Class<?> enumParticleClass = Class.forName(
                "net.minecraft.server." + version + ".EnumParticle"
            );

            // converte Bukkit#name → NMS enum
            String name = bukkitName.toUpperCase(Locale.ROOT);
            name = name.replace("_",""); // remove underscores pra comparação

            // casos especiais
            if (name.equals("HAPPYVILLAGER"))      name = "VILLAGER_HAPPY";
            else if (name.equals("ANGRYVILLAGER")) name = "VILLAGER_ANGRY";
            else {
                // tenta achar por “ignore underscore”
                for (Object constant : enumParticleClass.getEnumConstants()) {
                    String c = ((Enum<?>)constant).name().replace("_","");
                    if (c.equalsIgnoreCase(name)) {
                        name = ((Enum<?>)constant).name();
                        break;
                    }
                }
            }

            Object enumParticle = Enum.valueOf(
                (Class<Enum>)enumParticleClass,
                name
            );

            Constructor<?> constructor = packetClass.getConstructor(
                enumParticleClass, boolean.class,
                float.class, float.class, float.class,
                float.class, float.class, float.class,
                float.class,    // speed
                int.class,      // count
                int[].class     // data varargs
            );

            Object packet = constructor.newInstance(
                enumParticle,
                true,                 // longDistance
                (float)loc.getX(),
                (float)loc.getY(),
                (float)loc.getZ(),
                (float)dx, (float)dy, (float)dz,
                (float)speed,
                count,
                new int[0]            // varargs
            );

            // envia pra conexão do player
            Method  mGetHandle = player.getClass().getMethod("getHandle");
            Object  nmsPlayer  = mGetHandle.invoke(player);
            Field   fConn      = nmsPlayer.getClass().getField("playerConnection");
            Object  conn       = fConn.get(nmsPlayer);

            Class<?> packetInterface = Class.forName(
                "net.minecraft.server." + version + ".Packet"
            );
            Method mSend = conn.getClass()
                    .getMethod("sendPacket", packetInterface);

            mSend.invoke(conn, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

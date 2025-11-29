package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.Core;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public class VanishAPI {

    private static final boolean NEW_API;
    private static Method hideMethod;
    private static Method showMethod;

    static {
        boolean newApiDetected;

        try {
            hideMethod = Player.class.getMethod("hidePlayer", Plugin.class, Player.class);
            showMethod = Player.class.getMethod("showPlayer", Plugin.class, Player.class);
            newApiDetected = true;
        } catch (Exception e) {
            newApiDetected = false;
        }

        NEW_API = newApiDetected;
    }

    public static void hide(Player viewer, Player target) {
        try {
            if (NEW_API) {
                hideMethod.invoke(viewer, Core.getInstance(), target);
            } else {
                viewer.hidePlayer(target); // 1.8.8
            }
        } catch (Exception ignored) {}
    }

    public static void show(Player viewer, Player target) {
        try {
            if (NEW_API) {
                showMethod.invoke(viewer, Core.getInstance(), target);
            } else {
                viewer.showPlayer(target); // 1.8.8
            }
        } catch (Exception ignored) {}
    }
}

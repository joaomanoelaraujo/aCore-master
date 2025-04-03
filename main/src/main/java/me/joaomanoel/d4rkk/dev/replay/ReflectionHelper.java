/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ReflectionHelper {
    private static ReflectionHelper instance;
    private Class<?> materialClass;
    private Class<?> blockClass;
    private Class<?> blockDataClass;
    private Class<?> playerClass;
    private Class<?> entityToggleSwimEventClass;
    private Method matchMaterial;
    private Method getBlockData;
    private Method blockDataGetMaterial;
    private Method sendTitle;

    private ReflectionHelper() {
        this.initializeClasses();
        this.initializeMethods();
    }

    private void initializeClasses() {
        try {
            this.materialClass = Class.forName("org.bukkit.Material");
            this.blockClass = Class.forName("org.bukkit.block.Block");
            this.blockDataClass = Class.forName("org.bukkit.block.data.BlockData");
            this.playerClass = Class.forName("org.bukkit.entity.Player");
            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13)) {
                this.entityToggleSwimEventClass = Class.forName("org.bukkit.event.entity.EntityToggleSwimEvent");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeMethods() {
        try {
            this.matchMaterial = this.materialClass.getMethod("matchMaterial", String.class, Boolean.TYPE);
            this.getBlockData = this.blockClass.getMethod("getBlockData", new Class[0]);
            this.blockDataGetMaterial = this.blockDataClass.getMethod("getMaterial", new Class[0]);
            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
                this.sendTitle = this.playerClass.getMethod("sendTitle", String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public Object matchMaterial(String material, boolean legacy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return this.matchMaterial.invoke(null, material, legacy);
    }

    public Object getBlockData(Block block) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return this.getBlockData.invoke(block, new Object[0]);
    }

    public Object getBlockDataMaterial(Object blockData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return this.blockDataGetMaterial.invoke(blockData, new Object[0]);
    }

    public Class<? extends Event> getSwimEvent() {
        return (Class<? extends Event>) this.entityToggleSwimEventClass;
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            this.sendTitle.invoke(player, title, subtitle, fadeIn, stay, fadeOut);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public <T extends Event> void registerEvent(Class<T> event, Listener listener, Consumer<T> handler) {
        Bukkit.getPluginManager().registerEvent(event, listener, EventPriority.MONITOR, (l, e) -> {
            if (e.getClass().equals(event)) {
                handler.accept((T) e);
            }
        }, Core.getInstance(), true);
    }

    public static ReflectionHelper getInstance() {
        if (instance == null) {
            instance = new ReflectionHelper();
        }
        return instance;
    }
}


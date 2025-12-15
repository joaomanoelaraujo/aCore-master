/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class AbstractListener
implements Listener {
    protected Core plugin = Core.getInstance();

    public void register() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
    }

    public void unregister() {
        HandlerList.unregisterAll((Listener)this);
    }
}


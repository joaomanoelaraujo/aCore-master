/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoReconnector
extends BukkitRunnable {
    protected Plugin plugin;

    public AutoReconnector(Plugin plugin) {
        this.plugin = plugin;
        this.runTaskTimerAsynchronously(plugin, 1200L, 1200L);
    }

    public void run() {
        MySQLDatabase database = (MySQLDatabase)DatabaseRegistry.getDatabase();
        database.update("USE `" + database.getDatabase() + "`");
    }
}


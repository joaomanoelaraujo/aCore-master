package me.joaomanoel.d4rkk.dev.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginDependencyChecker {

    public static List<String> getPluginsUsingCore() {
        List<String> dependents = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getDescription().getDepend().contains("aCore") || plugin.getDescription().getSoftDepend().contains("aCore")) {
                dependents.add(plugin.getName() + " §7(v" + plugin.getDescription().getVersion() + ")");
            }
        }
        return dependents;
    }
}
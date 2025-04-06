package me.joaomanoel.d4rkk.dev.libraries.holograms;

import com.google.common.collect.ImmutableList;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.Hologram;
import me.joaomanoel.d4rkk.dev.libraries.holograms.api.HologramLine;
import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.plugin.KPlugin;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HologramLibrary {
  
  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("HOLOGRAMS");
  private static final List<Hologram> holograms = new ArrayList<>();
  private static Plugin plugin;
  
  public static Hologram createHologram(Location location, List<String> lines) {
    return createHologram(location, lines.toArray(new String[0]));
  }
  
  public static Hologram createHologram(Location location, String... lines) {
    return createHologram(location, true, lines);
  }
  
  public static Hologram createHologram(Location location, boolean spawn, String... lines) {
    Hologram hologram = new Hologram(location, lines);
    if (spawn) {
      hologram.spawn();
    }
    holograms.add(hologram);
    return hologram;
  }
  
  public static void removeHologram(Hologram hologram) {
    holograms.remove(hologram);
    hologram.despawn();
  }
  
  public static void unregisterAll() {
    holograms.forEach(Hologram::despawn);
    holograms.clear();
    plugin = null;
  }
  
  public static Entity getHologramEntity(int entityId) {
    for (Hologram hologram : listHolograms()) {
      if (hologram.isSpawned()) {
        for (HologramLine line : hologram.getLines()) {
          if (line.getArmor() != null && line.getArmor().getId() == entityId) {
            return line.getArmor().getEntity();
          }
        }
      }
    }
    
    return null;
  }
  
  public static Hologram getHologram(Entity entity) {
    return NMS.getHologram(entity);
  }
  
  public static boolean isHologramEntity(Entity entity) {
    return NMS.isHologramEntity(entity);
  }
  
  public static Collection<Hologram> listHolograms() {
    return ImmutableList.copyOf(holograms);
  }
  
  public static void setupHolograms(KPlugin pl) {
    if (plugin != null) {
      return;
    }
    
    plugin = pl;
    Bukkit.getPluginManager().registerEvents(new HologramListeners(), plugin);
  }
  
  public static Plugin getPlugin() {
    return plugin;
  }
}

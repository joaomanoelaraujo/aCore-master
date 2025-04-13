package me.joaomanoel.d4rkk.dev.libraries.hologram;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.libraries.hologram.listeners.HologramTouchEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class Hologram implements Listener {

    private Location location;
    private List<HologramLine> lines = new ArrayList<>();

    public Hologram(Location location) {
        this.location = location.clone();
    }

    public void registerHologramListener() {
        Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
    }

    public void appendLine(String content) {
        this.lines.add(new HologramLine(this.lines.size() + 1, content, this.location));
    }

    public void spawn() {
        new ArrayList<>(this.lines).forEach(HologramLine::spawn);
    }

    public List<HologramLine> listLines() {
        return this.lines;
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        this.lines.forEach(HologramLine::destroy);
        this.lines.clear();
        this.lines = null;
        this.location = null;
    }

    public void updateLine(int id, String content) {
        HologramLine line = this.lines.get(id);
        if (line != null) {
            line.update(content);
        }
    }

    public HologramLine findLineByID(int id) {
        return lines.stream().filter(hologramLine -> hologramLine.getIndex().equals(id)).findFirst().orElse(null);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
       Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity instanceof ArmorStand) {
            Location entityLocation = entity.getLocation();
            HologramLine line = this.lines.stream().filter(hologramLine -> {
                Location lineLoc = hologramLine.getLocation();
                return lineLoc.getX() == entityLocation.getX() && lineLoc.getZ() == entityLocation.getZ() && lineLoc.getY() == entityLocation.getY();
            }).findFirst().orElse(null);
            if (line != null) {
                HologramTouchEvent instanceEvent = new HologramTouchEvent(this, line, player);
                Bukkit.getPluginManager().callEvent(instanceEvent);
            }
        }
    }
}

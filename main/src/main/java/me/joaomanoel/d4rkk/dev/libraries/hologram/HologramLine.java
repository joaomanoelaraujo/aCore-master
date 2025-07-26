package me.joaomanoel.d4rkk.dev.libraries.hologram;

import me.joaomanoel.d4rkk.dev.nms.NMSManager;
import me.joaomanoel.d4rkk.dev.nms.hologram.HologramEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class HologramLine {

    private Integer index;
    private String content;
    private Location location;
    private HologramEntity entity;
    private Consumer<Player> touchHandler;

    public HologramLine(Integer index, String content, Location location) {
        this.index = index;
        this.content = content;
        this.location = location.clone().add(0, 0.3 * index, 0);
    }

    public void spawn() {
        this.entity = NMSManager.createHologram(this.location);
        this.entity.spawn(content);
    }

    public void destroy() {
        this.entity.kill();
        this.entity = null;
        this.index = null;
        this.content = null;
        this.location = null;
    }

    public void update(String content) {
        this.content = content;
        this.entity.updateContent(this.content);
    }


    public Integer getIndex() {
        return index;
    }

    public String getContent() {
        return content;
    }

    public Location getLocation() {
        return location;
    }

    public HologramEntity getEntity() {
        return entity;
    }
}

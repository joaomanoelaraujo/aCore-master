/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationData
implements Serializable {
    private static final long serialVersionUID = -849472505875330147L;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String world;

    public LocationData(double x, double y, double z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public float getPitch() {
        return this.pitch;
    }

    public String getWorld() {
        return this.world;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public float getYaw() {
        return this.yaw;
    }

    public double getZ() {
        return this.z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public static LocationData fromLocation(Location loc) {
        return new LocationData(loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName());
    }

    public static Location toLocation(LocationData locationData) {
        return new Location(Bukkit.getWorld((String)locationData.getWorld()), locationData.getX(), locationData.getY(), locationData.getZ());
    }

    public String toString() {
        return "LocationData{x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", pitch=" + this.pitch + ", world='" + this.world + '\'' + '}';
    }
}


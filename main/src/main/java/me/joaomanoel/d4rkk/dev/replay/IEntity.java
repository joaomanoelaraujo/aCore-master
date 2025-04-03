/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.WrappedDataWatcher
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IEntity {
    public void spawn(Location var1, Player ... var2);

    public void respawn(Player ... var1);

    public void despawn();

    public void remove();

    public void teleport(Location var1, boolean var2);

    public void move(Location var1, boolean var2, float var3, float var4);

    public void look(float var1, float var2);

    public void updateMetadata();

    public void animate(int var1);

    public int getId();

    public void setId(int var1);

    public void setData(WrappedDataWatcher var1);

    public WrappedDataWatcher getData();

    public void setPitch(float var1);

    public void setYaw(float var1);

    public Location getLocation();

    public void setOrigin(Location var1);

    public void setLocation(Location var1);

    public Location getOrigin();

    public Player[] getVisible();
}


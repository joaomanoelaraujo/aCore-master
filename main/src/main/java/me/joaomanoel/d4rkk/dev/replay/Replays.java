/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Replays {
    private final Game<?> game;
    private final String name;

    public Replays(Game<?> game, String name) {
        this.game = game;
        this.name = name + "-id-" + System.nanoTime() + "-created-2022";
    }

    public Game<?> getGame() {
        return this.game;
    }

    public String getName() {
        return this.name;
    }

    public void createReplay() {
        ReplayAPI.getInstance().recordReplay(this.name, this.game.listPlayers());
    }

    public void stopReplay() {
        ReplayAPI.getInstance().stopReplay(this.name, true);
    }

    public static List<ItemStack> getItem(Player player) {
        try {
            ArrayList<ItemStack> item = new ArrayList<ItemStack>();
            MySQLDatabase database = ConfigManager.getMySQLDatabase();
            PreparedStatement pst = database.getConnection().prepareStatement("SELECT id,creator,duration,time FROM replays");
            ResultSet rs = database.query(pst);
            while (rs.next()) {
                String id = rs.getString("id");
                String creator = rs.getString("creator");
                int duration = rs.getInt("duration");
                long time = rs.getLong("time");
                if (!Bukkit.getPlayer(creator).equals(player)) continue;
                item.add(BukkitUtils.deserializeItemStack("PAPER : 1 : name>&aReplay #" + id + " : desc>&7\n&7Duracao: &e" + duration + "\n\n&eClique para assistir!"));
                return item;
            }
            pst.close();
        } catch (Exception e) {
            Core.getInstance().getLogger().info(e.getMessage());
        }
        return null;
    }

    public static List<ReplayInfo> getReplays() {
        ArrayList<ReplayInfo> replays = new ArrayList<ReplayInfo>();
        try {
            MySQLDatabase database = ConfigManager.getMySQLDatabase();
            PreparedStatement pst = database.getConnection().prepareStatement("SELECT id,creator,duration,time FROM replays");
            ResultSet rs = database.query(pst);
            while (rs.next()) {
                String id = rs.getString("id");
                List<String> creator = Collections.singletonList(rs.getString("creator"));
                int duration = rs.getInt("duration");
                long time = rs.getLong("time");
                replays.add(new ReplayInfo(id, creator, time, duration));
            }
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return replays;
    }
}


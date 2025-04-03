/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySQLService
extends DatabaseService {
    private final MySQLDatabase database;
    private String table = "replays";

    public MySQLService(MySQLDatabase database, String prefix) {
        if (prefix != null && !prefix.isEmpty()) {
            this.table = prefix + this.table;
        }
        this.database = database;
    }

    @Override
    public void createReplayTable() {
        this.database.update("CREATE TABLE IF NOT EXISTS " + this.table + " (id VARCHAR(40) PRIMARY KEY UNIQUE, creator VARCHAR(30), duration INT(255), time BIGINT(255), data LONGBLOB)");
    }

    @Override
    public void addReplay(String id, List<String> creator, int duration, Long time, byte[] data) throws SQLException {
        final PreparedStatement pst = this.database.getConnection().prepareStatement("INSERT INTO " + this.table + " (id, creator, duration, time, data) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE creator = ?, duration = ?, time = ?, data = ?");
        pst.setString(1, id);
        pst.setString(2, creator.toString());
        pst.setInt(3, duration);
        pst.setLong(4, time);
        pst.setBytes(5, data);
        pst.setString(6, creator.toString());
        pst.setInt(7, duration);
        pst.setLong(8, time);
        pst.setBytes(9, data);
        this.pool.execute(() -> MySQLService.this.database.update(pst));
    }

    @Override
    public byte[] getReplayData(String id) {
        try {
            PreparedStatement pst = this.database.getConnection().prepareStatement("SELECT data FROM " + this.table + " WHERE id = ?");
            pst.setString(1, id);
            ResultSet rs = this.database.query(pst);
            if (rs.next()) {
                return rs.getBytes(1);
            }
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteReplay(String id) {
        try {
            final PreparedStatement pst = this.database.getConnection().prepareStatement("DELETE FROM " + this.table + " WHERE id = ?");
            pst.setString(1, id);
            this.pool.execute(() -> MySQLService.this.database.update(pst));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String id) {
        try {
            PreparedStatement pst = this.database.getConnection().prepareStatement("SELECT COUNT(1) FROM " + this.table + " WHERE id = ?");
            pst.setString(1, id);
            ResultSet rs = this.database.query(pst);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ReplayInfo> getReplays() {
        ArrayList<ReplayInfo> replays = new ArrayList<ReplayInfo>();
        try {
            PreparedStatement pst = this.database.getConnection().prepareStatement("SELECT id,creator,duration,time FROM " + this.table);
            ResultSet rs = this.database.query(pst);
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


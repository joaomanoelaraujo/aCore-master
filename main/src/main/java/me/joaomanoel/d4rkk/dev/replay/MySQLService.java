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

public class MySQLService extends DatabaseService {
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
        this.database.update(
                "CREATE TABLE IF NOT EXISTS " + this.table + " (" +
                        "id VARCHAR(64) PRIMARY KEY UNIQUE, " +
                        "creator TEXT, " +
                        "duration INT, " +
                        "time BIGINT, " +
                        "data LONGBLOB, " +
                        "game_name VARCHAR(50), " +
                        "mode VARCHAR(50), " +
                        "map VARCHAR(50), " +
                        "server VARCHAR(50), " +
                        "players INT DEFAULT 0" +
                        ")"
        );    }


    @Override
    public void addReplay(String id, List<String> creator, int duration, Long time, byte[] data, String gameName, String mode, String map, String server, int players) throws SQLException {

        final PreparedStatement pst = this.database.getConnection().prepareStatement(
                "INSERT INTO " + this.table + " (id, creator, duration, time, data, game_name, mode, map, server, players) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "creator = ?, duration = ?, time = ?, data = ?, game_name = ?, mode = ?, map = ?, server = ?, players = ?"
        );

        pst.setString(1, id);
        pst.setString(2, creator.toString());
        pst.setInt(3, duration);
        pst.setLong(4, time);
        pst.setBytes(5, data);
        pst.setString(6, gameName);
        pst.setString(7, mode);
        pst.setString(8, map);
        pst.setString(9, server);
        pst.setInt(10, players);

        pst.setString(11, creator.toString());
        pst.setInt(12, duration);
        pst.setLong(13, time);
        pst.setBytes(14, data);
        pst.setString(15, gameName);
        pst.setString(16, mode);
        pst.setString(17, map);
        pst.setString(18, server);
        pst.setInt(19, players);

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
            this.pool.execute(new Runnable(){

                @Override
                public void run() {
                    MySQLService.this.database.update(pst);
                }
            });
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
        ArrayList<ReplayInfo> replays = new ArrayList<>();

        try {
            PreparedStatement pst = this.database.getConnection().prepareStatement(
                    "SELECT id, creator, duration, time, game_name, mode, map, server, players FROM " + this.table
            );
            ResultSet rs = this.database.query(pst);

            while (rs.next()) {
                String id = rs.getString("id");
                List<String> creator = Collections.singletonList(rs.getString("creator"));
                int duration = rs.getInt("duration");
                long time = rs.getLong("time");
                String gameName = rs.getString("game_name");
                String mode = rs.getString("mode");
                String map = rs.getString("map");
                String server = rs.getString("server");
                int players = rs.getInt("players");

                ReplayInfo info = new ReplayInfo(id, creator, time, duration, gameName, mode, map, server, players);
                replays.add(info);
            }

            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return replays;
    }

}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDatabase
extends Database {
    private Connection connection;
    private final MySQLService service;

    public MySQLDatabase(String host, int port, String database, String user, String password, String prefix) {
        super(host, port, database, user, password);
        this.service = new MySQLService(this, prefix);
        new AutoReconnector(Core.getInstance());
    }

    @Override
    public String getDataSourceName() {
        return String.format("jdbc:mysql://%s:%d/%s?useSSL=false&characterEncoding=latin1", this.host, this.port, this.database);
    }

    @Override
    public void connect() {
        try {
            String dsn = this.getDataSourceName();
            this.connection = DriverManager.getConnection(dsn, this.user, this.password);
            LogUtils.log("Successfully conntected to MySQL database");
        } catch (SQLException e) {
            LogUtils.log("Unable to connect to MySQL database: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        try {
            if (this.connection != null) {
                this.connection.close();
                LogUtils.log("Connection closed");
            }
        } catch (SQLException e) {
            LogUtils.log("Error while closing the connection: " + e.getMessage());
        }
    }

    @Override
    public DatabaseService getService() {
        return this.service;
    }

    public void update(PreparedStatement pst) {
        try {
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            this.connect();
            System.err.println(e);
        }
    }

    public void update(String qry) {
        try {
            Statement st = this.connection.createStatement();
            st.executeUpdate(qry);
            st.close();
        } catch (SQLException e) {
            this.connect();
            System.err.println(e);
        }
    }

    public ResultSet query(PreparedStatement pst) {
        ResultSet rs = null;
        try {
            rs = pst.executeQuery();
        } catch (SQLException e) {
            this.connect();
            System.err.println(e);
        }
        return rs;
    }

    public boolean hasConnection() {
        try {
            return this.connection != null || this.connection.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }

    public String getDatabase() {
        return this.database;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeRessources(ResultSet rs, PreparedStatement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sQLException) {
                // empty catch block
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


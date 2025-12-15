/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public abstract class Database {
    protected String host;
    protected int port;
    protected String database;
    protected String user;
    protected String password;

    public Database(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.connect();
    }

    public abstract void connect();

    public abstract void disconnect();

    public abstract DatabaseService getService();

    public abstract String getDataSourceName();
}


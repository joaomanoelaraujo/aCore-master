/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DatabaseService {
    protected ExecutorService pool = Executors.newCachedThreadPool();

    public abstract void createReplayTable();

    public abstract void addReplay(String id, List<String> creator, int duration, Long time, byte[] data,
                                   String gameName, String mode, String map, String server, int players) throws SQLException;

    public abstract byte[] getReplayData(String var1);

    public abstract void deleteReplay(String var1);

    public abstract boolean exists(String var1);

    public abstract List<ReplayInfo> getReplays();

    public ExecutorService getPool() {
        return this.pool;
    }
}


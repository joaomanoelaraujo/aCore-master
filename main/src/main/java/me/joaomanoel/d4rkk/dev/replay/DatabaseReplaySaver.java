/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DatabaseReplaySaver
implements IReplaySaver {
    public static HashMap<String, ReplayInfo> replayCache;

    @Override
    public void saveReplay(Replay replay) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            GZIPOutputStream gOut = new GZIPOutputStream(byteOut);
            ObjectOutputStream objectOut = new ObjectOutputStream(gOut);
            objectOut.writeObject(replay.getData());
            objectOut.flush();
            gOut.close();
            byteOut.close();
            objectOut.close();
            byte[] data = byteOut.toByteArray();
            if (replay.getReplayInfo() == null) {
                replay.setReplayInfo(new ReplayInfo(replay.getId(), null, System.currentTimeMillis(), replay.getData().getDuration()));
            }
            DatabaseRegistry.getDatabase().getService().addReplay(replay.getId(), replay.getReplayInfo().getCreator(), replay.getReplayInfo().getDuration(), replay.getReplayInfo().getTime(), data);
            this.updateCache(replay.getId(), replay.getReplayInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadReplay(final String replayName, Consumer<Replay> consumer) {
        DatabaseRegistry.getDatabase().getService().getPool().execute(new Acceptor<Replay>(consumer){

            @Override
            public Replay getValue() {
                try {
                    byte[] data = DatabaseRegistry.getDatabase().getService().getReplayData(replayName);
                    ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
                    GZIPInputStream gIn = new GZIPInputStream(byteIn);
                    ObjectInputStream objectIn = new ObjectInputStream(gIn);
                    ReplayData replayData = (ReplayData)objectIn.readObject();
                    objectIn.close();
                    gIn.close();
                    byteIn.close();
                    return new Replay(replayName, replayData);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    @Override
    public boolean replayExists(String replayName) {
        if (replayCache != null) {
            return replayCache.containsKey(replayName);
        }
        return DatabaseRegistry.getDatabase().getService().exists(replayName);
    }

    @Override
    public void deleteReplay(String replayName) {
        DatabaseRegistry.getDatabase().getService().deleteReplay(replayName);
        this.updateCache(replayName, null);
    }

    @Override
    public List<String> getReplays() {
        if (replayCache == null) {
            replayCache = new HashMap<>();
            DatabaseRegistry.getDatabase().getService().getReplays().forEach(info -> replayCache.put(info.getID(), info));
        }
        return new ArrayList<>(replayCache.keySet());
    }

    public static ReplayInfo getInfo(String replay) {
        if (replayCache != null && replayCache.containsKey(replay)) {
            return replayCache.get(replay);
        }
        return null;
    }

    private void updateCache(String id, ReplayInfo info) {
        if (replayCache == null) {
            replayCache = new HashMap<>();
        }
        if (info != null && id != null) {
            replayCache.put(id, info);
        } else replayCache.remove(id);
    }
}


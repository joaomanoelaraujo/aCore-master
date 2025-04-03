/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.ArrayList;
import java.util.List;

public class ReplaySaver {
    public static IReplaySaver replaySaver;

    public static void register(IReplaySaver saver) {
        replaySaver = saver;
    }

    public static void unregister() {
        replaySaver = null;
    }

    public static boolean isRegistered() {
        return replaySaver != null;
    }

    public static void save(Replay replay) {
        if (ReplaySaver.isRegistered()) {
            replaySaver.saveReplay(replay);
        }
    }

    public static void load(String replayName, Consumer<Replay> consumer) {
        if (ReplaySaver.isRegistered()) {
            replaySaver.loadReplay(replayName, consumer);
        } else {
            consumer.accept(null);
        }
    }

    public static boolean exists(String replayName) {
        if (ReplaySaver.isRegistered()) {
            return replaySaver.replayExists(replayName);
        }
        return false;
    }

    public static void delete(String replayName) {
        if (ReplaySaver.isRegistered()) {
            replaySaver.deleteReplay(replayName);
        }
    }

    public static List<String> getReplays() {
        if (ReplaySaver.isRegistered()) {
            return replaySaver.getReplays();
        }
        return new ArrayList<String>();
    }
}


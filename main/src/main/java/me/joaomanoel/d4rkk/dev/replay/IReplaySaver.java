/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.List;

public interface IReplaySaver {
    public void saveReplay(Replay var1);

    public void loadReplay(String var1, Consumer<Replay> var2);

    public boolean replayExists(String var1);

    public void deleteReplay(String var1);

    public List<String> getReplays();
}


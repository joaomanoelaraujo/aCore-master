/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.replay.ActionData;
import me.joaomanoel.d4rkk.dev.replay.PacketData;
import me.joaomanoel.d4rkk.dev.replay.Replayer;
import java.util.List;

public interface IReplayHook {
    public List<PacketData> onRecord(String var1);

    public void onPlay(ActionData var1, Replayer var2);
}


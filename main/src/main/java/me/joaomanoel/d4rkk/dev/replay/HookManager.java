/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.replay.IReplayHook;
import java.util.ArrayList;
import java.util.List;

public class HookManager {
    private final List<IReplayHook> hooks = new ArrayList<IReplayHook>();

    public void registerHook(IReplayHook hook) {
        if (!this.hooks.contains(hook)) {
            this.hooks.add(hook);
        }
    }

    public void unregisterHook(IReplayHook hook) {
        this.hooks.remove(hook);
    }

    public boolean isRegistered() {
        return !this.hooks.isEmpty();
    }

    public List<IReplayHook> getHooks() {
        return this.hooks;
    }
}


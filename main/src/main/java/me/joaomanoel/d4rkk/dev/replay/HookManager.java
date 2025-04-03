/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.ArrayList;
import java.util.List;

public class HookManager {
    private List<IReplayHook> hooks = new ArrayList<IReplayHook>();

    public void registerHook(IReplayHook hook) {
        if (!this.hooks.contains(hook)) {
            this.hooks.add(hook);
        }
    }

    public void unregisterHook(IReplayHook hook) {
        if (this.hooks.contains(hook)) {
            this.hooks.remove(hook);
        }
    }

    public boolean isRegistered() {
        return this.hooks.size() > 0;
    }

    public List<IReplayHook> getHooks() {
        return this.hooks;
    }
}


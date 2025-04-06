/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReplayStats {
    private Map<String, Long> actions;
    private List<String> players;
    private long entityCount;

    public ReplayStats(Map<String, Long> actions, List<String> players, long entityCount) {
        this.actions = actions;
        this.players = players;
        this.entityCount = entityCount;
    }

    public long getActionCount() {
        return this.actions.values().stream().reduce(0L, Long::sum);
    }

    public Map<Object, Object> getSortedActions() {
        return this.actions.entrySet().stream().sorted().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
    }

    public Map<String, Long> getActions() {
        return this.actions;
    }

    public List<String> getPlayers() {
        return this.players;
    }

    public long getEntityCount() {
        return this.entityCount;
    }
}


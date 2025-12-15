package me.joaomanoel.d4rkk.dev.replay;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReplayStats {
    private final Map<String, Long> actions;
    private final List<String> players;
    private final long entityCount;

    public ReplayStats(Map<String, Long> actions, List<String> players, long entityCount) {
        this.actions = actions;
        this.players = players;
        this.entityCount = entityCount;
    }

    public long getActionCount() {
        return this.actions.values().stream().reduce(0L, Long::sum);
    }

    public Map<String, Long> getSortedActions() {
        return this.actions.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> n,
                        LinkedHashMap::new
                ));
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

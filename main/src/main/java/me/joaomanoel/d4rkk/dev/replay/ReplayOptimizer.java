/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReplayOptimizer {
    private MovingData lastMovement;
    private int playerCount;
    private int entityMovementCount;
    private int velocityCount;

    public boolean shouldRecord(PacketData data) {
        if (data instanceof MovingData) {
            return this.shouldRecordPlayerMovement((MovingData)data);
        }
        if (data instanceof EntityMovingData || data instanceof VelocityData) {
            return this.shouldRecordEntityMovement(data);
        }
        return true;
    }

    public boolean shouldRecordPlayerMovement(MovingData data) {
        if (this.lastMovement == null) {
            this.lastMovement = data;
            return true;
        }
        this.playerCount = this.playerCount >= this.countToRemove() ? 0 : this.playerCount + 1;
        return this.calculateDifference(data) > this.requiredDifference() && (ConfigManager.QUALITY == ReplayQuality.HIGH || this.playerCount != this.countToRemove());
    }

    public boolean shouldRecordEntityMovement(PacketData data) {
        if (ConfigManager.QUALITY == ReplayQuality.HIGH) {
            return true;
        }
        boolean isMovingData = data instanceof EntityMovingData;
        if (isMovingData) {
            this.entityMovementCount = this.entityMovementCount >= this.countToRemove() ? 0 : this.entityMovementCount + 1;
        } else {
            this.velocityCount = this.velocityCount >= this.countToRemove() ? 0 : this.velocityCount + 1;
        }
        return this.entityMovementCount != this.countToRemove() && isMovingData || this.velocityCount != this.countToRemove() && !isMovingData;
    }

    public int countToRemove() {
        if (ConfigManager.QUALITY == ReplayQuality.LOW) {
            return 1;
        }
        if (ConfigManager.QUALITY == ReplayQuality.MEDIUM) {
            return 4;
        }
        return -1;
    }

    public double requiredDifference() {
        if (ConfigManager.QUALITY == ReplayQuality.LOW) {
            return 0.06;
        }
        if (ConfigManager.QUALITY == ReplayQuality.MEDIUM) {
            return 0.05;
        }
        return 0.0;
    }

    public double calculateDifference(MovingData data) {
        double locationDiff = Math.abs(data.getX() - this.lastMovement.getX()) + Math.abs(data.getY() - this.lastMovement.getY()) + Math.abs(data.getZ() - this.lastMovement.getZ());
        double rotationDiff = Math.abs(data.getYaw() - this.lastMovement.getYaw()) + Math.abs(data.getPitch() - this.lastMovement.getPitch());
        this.lastMovement = data;
        return locationDiff + rotationDiff;
    }

        private String name;

        public String getName() {
            return name;
        }


    public static ReplayStats analyzeReplay(Replay replay) {
        HashMap<Integer, List<ActionData>> data = replay.getData().getActions();
        ArrayList<ActionData> merged = new ArrayList<>();
        data.values().forEach(merged::addAll);

        long entityCount = merged.stream()
                .filter(action -> action.getPacketData() instanceof EntityData)
                .filter(action -> ((EntityData)action.getPacketData()).getAction() == 0)
                .count();

        List<String> players = merged.stream()
                .map(ActionData::getName)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Long> actions = merged.stream()
                .filter(action -> action.getPacketData() != null)
                .map(action -> action.getPacketData().getClass().getSimpleName())
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        return new ReplayStats(actions, players, entityCount);
    }

}


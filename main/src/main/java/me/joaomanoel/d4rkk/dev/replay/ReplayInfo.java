package me.joaomanoel.d4rkk.dev.replay;

import java.util.List;

public class ReplayInfo {
    private final String id;
    private final List<String> creator;
    private final long time;
    private final int duration;
    private String gameName;
    private String mode;
    private String map;
    private String server;
    private int players;

    public ReplayInfo(String id, List<String> creator, long time, int duration, String gameName, String mode, String map, String server, int players) {
        this.id = id;
        this.creator = creator;
        this.time = time;
        this.duration = duration;
        this.gameName = gameName;
        this.mode = mode;
        this.map = map;
        this.server = server;
        this.players = players;
    }

    public String getID() {
        return id;
    }

    public List<String> getCreator() {
        return creator;
    }

    public long getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }
}

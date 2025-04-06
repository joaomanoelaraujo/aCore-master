package me.joaomanoel.d4rkk.dev.game;

import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FakeGame implements Game<GameTeam> {
  
  public static final FakeGame FAKE_GAME = new FakeGame();
  protected List<Player> emptyList = new ArrayList<>(0);
  
  private FakeGame() {
  }
  
  @Override
  public void broadcastMessage(String s) {
  }
  
  @Override
  public void broadcastMessage(String s, boolean b) {
  }
  
  @Override
  public void join(Profile profile) {
  }
  
  @Override
  public void leave(Profile profile, Game<?> game) {
  }
  
  @Override
  public void kill(Profile profile, Profile profile1) {
  }
  
  @Override
  public void killLeave(Profile profile, Profile profile1) {
  }
  
  @Override
  public void start() {
  }
  
  @Override
  public void stop(GameTeam gameTeam) {
  }
  
  @Override
  public void reset() {
  }
  
  @Override
  public String getGameName() {
    return "FakeGame";
  }
  
  @Override
  public GameState getState() {
    return GameState.AGUARDANDO;
  }
  
  @Override
  public boolean isSpectator(Player player) {
    return false;
  }
  
  @Override
  public int getOnline() {
    return 0;
  }
  
  @Override
  public int getMaxPlayers() {
    return 0;
  }
  
  @Override
  public GameTeam getTeam(Player player) {
    return null;
  }
  
  @Override
  public List<GameTeam> listTeams() {
    return null;
  }
  
  @Override
  public List<Player> listPlayers() {
    return emptyList;
  }
  
  @Override
  public List<Player> listPlayers(boolean b) {
    return emptyList;
  }
}

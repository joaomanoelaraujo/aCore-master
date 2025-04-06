package me.joaomanoel.d4rkk.dev.utils.queue;

import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.entity.Player;

public class QueuePlayer {
  
  public Player player;
  public Profile profile;
  public String server;
  
  public QueuePlayer(Player player, Profile profile, String server) {
    this.player = player;
    this.profile = profile;
    this.server = server;
  }
  
  public void destroy() {
    this.player = null;
    this.profile = null;
    this.server = null;
  }
}

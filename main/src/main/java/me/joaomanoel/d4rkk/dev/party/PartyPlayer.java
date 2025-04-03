package me.joaomanoel.d4rkk.dev.party;

import me.joaomanoel.d4rkk.dev.Manager;

public class PartyPlayer {
  
  private final String name;
  private PartyRole role;
  
  public PartyPlayer(String name, PartyRole role) {
    this.name = name;
    this.role = role;
  }
  
  public void sendMessage(String message) {
    Object player = Manager.getPlayer(name);
    if (player != null) {
      Manager.sendMessage(player, message);
    }
  }
  
  public String getName() {
    return this.name;
  }
  
  public PartyRole getRole() {
    return this.role;
  }
  
  public void setRole(PartyRole role) {
    this.role = role;
  }
  
  public boolean isOnline() {
    return Manager.getPlayer(this.name) != null;
  }
}

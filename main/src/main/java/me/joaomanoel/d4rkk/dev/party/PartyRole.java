package me.joaomanoel.d4rkk.dev.party;

public enum PartyRole {
  MEMBER("Membro"),
  LEADER("Líder");
  
  private final String name;
  
  PartyRole(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}

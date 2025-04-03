package me.joaomanoel.d4rkk.dev.player.enums;

public enum ProtectionLobby {
  ATIVADO,
  DESATIVADO;
  
  private static final ProtectionLobby[] VALUES = values();
  
  public static ProtectionLobby getByOrdinal(long ordinal) {
    if (ordinal < 2 && ordinal > -1) {
      return VALUES[(int) ordinal];
    }
    
    return null;
  }
  
  public String getInkSack() {
    if (this == ATIVADO) {
      return "10";
    }
    
    return "8";
  }
  
  public String getName() {
    if (this == ATIVADO) {
      return "§aON";
    }
    
    return "§cOFF";
  }
  
  public ProtectionLobby next() {
    if (this == DESATIVADO) {
      return ATIVADO;
    }
    
    return DESATIVADO;
  }
}

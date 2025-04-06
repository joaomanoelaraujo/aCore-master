package me.joaomanoel.d4rkk.dev.database.exception;

public class ProfileLoadException extends Exception {
  private static final long serialVersionUID = 3287516992918834123L;
  
  public ProfileLoadException(String reason) {
    super(reason);
  }
}

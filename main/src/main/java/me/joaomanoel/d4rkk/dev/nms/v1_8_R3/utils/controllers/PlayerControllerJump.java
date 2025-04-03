package me.joaomanoel.d4rkk.dev.nms.v1_8_R3.utils.controllers;

import me.joaomanoel.d4rkk.dev.nms.v1_8_R3.entity.EntityNPCPlayer;

public class PlayerControllerJump {
  
  private final EntityNPCPlayer player;
  private boolean b;
  
  public PlayerControllerJump(EntityNPCPlayer entityNPCPlayer) {
    this.player = entityNPCPlayer;
  }
  
  public void a() {
    this.b = true;
  }
  
  public void b() {
    this.player.i(this.b);
    this.b = false;
  }
}

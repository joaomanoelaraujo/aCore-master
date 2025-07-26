package me.joaomanoel.d4rkk.dev.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftVersion;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.hook.protocollib.npc.NPCAdapter_1_20_R4;
import me.joaomanoel.d4rkk.dev.hook.protocollib.npc.NPCAdapter_1_8_R3;

public class HologramAdapter extends PacketAdapter {

  public HologramAdapter() {
    super(params().plugin(Core.getInstance()).types(PacketType.Play.Server.ENTITY_STATUS, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.PLAYER_INFO));
  }
  
  @Override
  public void onPacketSending(PacketEvent evt) {
  }
  
  @Override
  public void onPacketReceiving(PacketEvent evt) {}

}

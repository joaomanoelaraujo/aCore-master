package me.joaomanoel.d4rkk.dev.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftVersion;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.hook.protocollib.npc.NPCAdapter_1_20_R4;
import me.joaomanoel.d4rkk.dev.hook.protocollib.npc.NPCAdapter_1_8_R3;

public class NPCAdapter extends PacketAdapter {

  private final NPCAdapter_1_8_R3 adapter_1_8_r3 = new NPCAdapter_1_8_R3();
  private final NPCAdapter_1_20_R4 adapter_1_20_r4 = new NPCAdapter_1_20_R4();

  public NPCAdapter() {
    super(params().plugin(Core.getInstance()).types(PacketType.Play.Server.ENTITY_STATUS, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.PLAYER_INFO));
  }
  
  @Override
  public void onPacketSending(PacketEvent evt) {
    MinecraftVersion version = ProtocolLibrary.getProtocolManager().getMinecraftVersion();
    if (version.getVersion().equals("1.8.8")) adapter_1_8_r3.onPacketSending(evt);
    else if (version.getVersion().equals("1.20.6")) adapter_1_20_r4.onPacketSending(evt);
  }
  
  @Override
  public void onPacketReceiving(PacketEvent evt) {}

}

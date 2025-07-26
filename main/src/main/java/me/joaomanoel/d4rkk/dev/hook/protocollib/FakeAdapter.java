package me.joaomanoel.d4rkk.dev.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftVersion;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.hook.protocollib.fake.FakeAdapter_1_20_R4;
import me.joaomanoel.d4rkk.dev.hook.protocollib.fake.FakeAdapter_1_8_R3;

import static com.comphenix.protocol.PacketType.Play.Server.*;

public class FakeAdapter extends PacketAdapter {

  private final FakeAdapter_1_8_R3 adapter_1_8_r3 = new FakeAdapter_1_8_R3();
  private final FakeAdapter_1_20_R4 adapter_1_20_r4 = new FakeAdapter_1_20_R4();

  public FakeAdapter() {
    super(params().plugin(Core.getInstance()).types(PacketType.Play.Client.CHAT, TAB_COMPLETE, PLAYER_INFO, SYSTEM_CHAT, SCOREBOARD_OBJECTIVE, SCOREBOARD_SCORE, SCOREBOARD_TEAM, PacketType.Play.Client.CHAT_COMMAND));
  }

  @Override
  public void onPacketReceiving(PacketEvent evt) {
    MinecraftVersion version = ProtocolLibrary.getProtocolManager().getMinecraftVersion();
    if (version.getVersion().equals("1.8.8")) adapter_1_8_r3.onPacketReceiving(evt);
    else if (version.getVersion().equals("1.20.6")) adapter_1_20_r4.onPacketReceiving(evt);
  }

  @Override
  public void onPacketSending(PacketEvent evt) {
    MinecraftVersion version = ProtocolLibrary.getProtocolManager().getMinecraftVersion();
    if (version.getVersion().equals("1.8.8")) adapter_1_8_r3.onPacketSending(evt);
    else if (version.getVersion().equals("1.20.6")) adapter_1_20_r4.onPacketSending(evt);
  }
}


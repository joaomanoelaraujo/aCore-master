package me.joaomanoel.d4rkk.dev.hook.protocollib;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.utility.MinecraftVersion;
import me.joaomanoel.d4rkk.dev.hook.protocollib.fake.FakeAdapter_1_20_R4;
import me.joaomanoel.d4rkk.dev.hook.protocollib.fake.FakeAdapter_1_8_R3;

public class FakeAdapter {

  public static void setup() {
    MinecraftVersion version = ProtocolLibrary.getProtocolManager().getMinecraftVersion();
    if (version.getVersion().equals("1.8.8")) ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter_1_8_R3());
    else if (version.getVersion().equals("1.20.6")) ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter_1_20_R4());
  }

}


package me.joaomanoel.d4rkk.dev.hook.protocollib.fake;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.joaomanoel.d4rkk.dev.player.fake.FakeManager;

import java.util.*;

public class FakeAdapter_1_20_R4 {

    public void onPacketReceiving(PacketEvent evt) {}

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        System.out.println(packet.getType().name());
        switch (packet.getType().name()) {
            case "SYSTEM_CHAT": {
                WrappedChatComponent chatComponent = event.getPacket().getChatComponents().read(0);
                if (chatComponent == null) {
                    return;
                }

                String jsonOriginal = chatComponent.getJson();
                event.getPacket().getChatComponents().write(0, WrappedChatComponent.fromJson(FakeManager.replaceNickedPlayers(jsonOriginal, true)));
                break;
            }

            case "PLAYER_INFO": {
                List<PlayerInfoData> entries = packet.getPlayerInfoDataLists().read(1);
                List<PlayerInfoData> newEntries = new ArrayList<>();
                for (PlayerInfoData entry : entries) {
                    WrappedGameProfile old = entry.getProfile();
                    if (!FakeManager.isFake(old.getName())) {
                        return;
                    }

                    WrappedGameProfile profile = new WrappedGameProfile(old.getId(), FakeManager.getFake(old.getName()));
                    PlayerInfoData newEntry = new PlayerInfoData(
                            profile,
                            entry.getLatency(),
                            entry.getGameMode(),
                            WrappedChatComponent.fromJson(profile.getName())
                    );

                    newEntries.add(newEntry);
                }

                packet.getPlayerInfoDataLists().write(1, newEntries);
                break;
            }

            case "SCOREBOARD_TEAM": {
                List<String> members = new ArrayList<>();
                for (String member : (Collection<String>) packet.getModifier().withType(Collection.class).read(0)) {
                    if (FakeManager.isFake(member)) {
                        member = FakeManager.getFake(member);
                    }

                    members.add(member);
                }

                packet.getModifier().withType(Collection.class).write(0, members);
                break;
            }

            case "SCOREBOARD_SCORE": {
                packet.getStrings().write(0, FakeManager.replaceNickedPlayers(packet.getStrings().read(0), true));
                break;
            }

            case "SCOREBOARD_OBJECTIVE": {
                packet.getStrings().write(1, FakeManager.replaceNickedPlayers(packet.getStrings().read(1), true));
                break;
            }
        }

    }

}

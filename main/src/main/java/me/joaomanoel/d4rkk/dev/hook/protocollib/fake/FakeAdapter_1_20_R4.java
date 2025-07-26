package me.joaomanoel.d4rkk.dev.hook.protocollib.fake;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.joaomanoel.d4rkk.dev.player.fake.FakeManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeAdapter_1_20_R4 {

    public void onPacketReceiving(PacketEvent evt) {
        PacketContainer packet = evt.getPacket();
        if (packet.getType().name().equals("CHAT_COMMAND")) {
            packet.getStrings().write(0, FakeManager.replaceNickedPlayers(packet.getStrings().read(0), false));
        }
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
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

            case "TAB_COMPLETE": {
                List<Object> modifiedList = new ArrayList<>();
                for (Object entry : packet.getSpecificModifier(List.class).read(0)) {
                    try {
                        Field textField = entry.getClass().getDeclaredField("text");
                        textField.setAccessible(true);
                        String text = (String) textField.get(entry);
                        if (FakeManager.isFake(text)) {
                            Constructor<?> constructor = entry.getClass().getDeclaredConstructor(String.class, Optional.class);
                            constructor.setAccessible(true);

                            Object newEntry = constructor.newInstance(FakeManager.getFake(text), Optional.empty());
                            modifiedList.add(newEntry);
                            continue;
                        }

                        modifiedList.add(entry);
                    } catch (Exception ignored) {}
                }

                packet.getSpecificModifier(List.class).write(0, modifiedList);
                break;
            }

            case "PLAYER_INFO": {
                List<PlayerInfoData> entries = packet.getPlayerInfoDataLists().read(1);
                List<PlayerInfoData> newEntries = new ArrayList<>();
                for (PlayerInfoData entry : entries) {
                    WrappedGameProfile old = entry.getProfile();
                    if (!FakeManager.isFake(old.getName())) {
                        newEntries.add(entry);
                        continue;
                    }

                    PlayerInfoData newEntry = new PlayerInfoData(
                            FakeManager.cloneProfile(old),
                            entry.getLatency(),
                            entry.getGameMode(),
                            entry.getDisplayName()
                    );

                    newEntries.add(newEntry);
                }

                packet.getPlayerInfoDataLists().write(1, newEntries);
                break;
            }
        }

    }

}

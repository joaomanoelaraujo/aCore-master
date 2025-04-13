package me.joaomanoel.d4rkk.dev.hook.protocollib.fake;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.joaomanoel.d4rkk.dev.player.fake.FakeManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeAdapter_1_20_R4 {

    public void onPacketReceiving(PacketEvent evt) {
        if (evt.getPacketType() == PacketType.Play.Client.CHAT) {
            PacketContainer packet = evt.getPacket();
            String msg = packet.getStrings().read(0);
            if (msg.startsWith("/")) {
                packet.getStrings().write(0, FakeManager.replaceNickedPlayers(msg, false));
            } else {
                packet.getStrings().write(0, FakeManager.replaceNickedChanges(msg));
            }
        }
    }

    public void onPacketSending(PacketEvent evt) {
        PacketContainer packet = evt.getPacket();

        switch (packet.getType().name()) {
            case "PLAY_SERVER_TAB_COMPLETE": {
                List<String> list = new ArrayList<>();
                for (String completion : packet.getStringArrays().read(0)) {
                    list.add(FakeManager.replaceNickedPlayers(completion, true));
                }
                packet.getStringArrays().write(0, list.toArray(new String[0]));
                break;
            }

            case "PLAY_SERVER_PLAYER_INFO": {
                List<PlayerInfoData> originalList = packet.getPlayerInfoDataLists().read(0);
                List<PlayerInfoData> newList = new ArrayList<>();

                for (PlayerInfoData data : originalList) {
                    if (data == null || data.getProfile() == null) continue;

                    WrappedGameProfile profile = data.getProfile();
                    if (FakeManager.isFake(profile.getName())) {
                        WrappedGameProfile newProfile = FakeManager.cloneProfile(profile);
                        data = new PlayerInfoData(newProfile, data.getLatency(), data.getGameMode(), data.getDisplayName());
                    }

                    newList.add(data);
                }

                packet.getPlayerInfoDataLists().write(0, newList);
                break;
            }

            case "PLAY_SERVER_CHAT": {
                WrappedChatComponent comp1 = packet.getChatComponents().readSafely(0);
                if (comp1 != null) {
                    String json = FakeManager.replaceNickedPlayers(comp1.getJson(), true);
                    packet.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
                }

                Object secondField = packet.getModifier().readSafely(1);
                if (secondField instanceof BaseComponent[]) {
                    BaseComponent[] components = (BaseComponent[]) secondField;
                    List<BaseComponent> updated = new ArrayList<>();
                    for (BaseComponent comp : components) {
                        String serialized = ComponentSerializer.toString(comp);
                        String replaced = FakeManager.replaceNickedPlayers(serialized, true);
                        BaseComponent[] parsed = ComponentSerializer.parse(replaced);

                        TextComponent combined = new TextComponent();
                        for (BaseComponent c : parsed) {
                            combined.addExtra(c);
                        }
                        updated.add(combined);
                    }

                    packet.getModifier().write(1, updated.toArray(new BaseComponent[0]));
                }
                break;
            }

            case "PLAY_SERVER_SCOREBOARD_OBJECTIVE": {
                String displayName = packet.getStrings().read(1);
                packet.getStrings().write(1, FakeManager.replaceNickedPlayers(displayName, true));
                break;
            }

            case "PLAY_SERVER_SCOREBOARD_SCORE": {
                String scoreName = packet.getStrings().read(0);
                packet.getStrings().write(0, FakeManager.replaceNickedPlayers(scoreName, true));
                break;
            }

            case "PLAY_SERVER_SCOREBOARD_TEAM": {
                Collection<String> members = packet.getSpecificModifier(Collection.class).read(0);
                List<String> newMembers = new ArrayList<>();
                for (String member : members) {
                    newMembers.add(FakeManager.isFake(member) ? FakeManager.getFake(member) : member);
                }
                packet.getSpecificModifier(Collection.class).write(0, newMembers);
                break;
            }
        }
    }

}

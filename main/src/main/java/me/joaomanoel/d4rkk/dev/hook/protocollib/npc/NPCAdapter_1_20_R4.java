package me.joaomanoel.d4rkk.dev.hook.protocollib.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.joaomanoel.d4rkk.dev.libraries.npc.NPCLibrary;
import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;

import java.util.ArrayList;
import java.util.List;

public class NPCAdapter_1_20_R4 {

    public void onPacketSending(PacketEvent evt) {
        PacketContainer packet = evt.getPacket();

        if (packet.getType() == PacketType.Play.Server.PLAYER_INFO) {
            List<PlayerInfoData> originalList = packet.getPlayerInfoDataLists().read(0);
            List<PlayerInfoData> modifiedList = new ArrayList<>();
            boolean needsClone = false;

            for (PlayerInfoData data : originalList) {
                if (data == null) continue;

                NpcEntity npc = NPCLibrary.findByUUID(data.getProfile().getUUID());

                if (npc != null && npc.isCopySkin()) {
                    WrappedGameProfile original = data.getProfile();
                    WrappedGameProfile playerProfile = WrappedGameProfile.fromPlayer(evt.getPlayer());

                    WrappedGameProfile cloned = new WrappedGameProfile(original.getUUID(), original.getName());
                    playerProfile.getProperties().get("textures").stream().findFirst().ifPresent(prop ->
                            cloned.getProperties().put("textures", prop)
                    );

                    PlayerInfoData newData = new PlayerInfoData(
                            cloned,
                            data.getLatency(),
                            data.getGameMode(),
                            data.getDisplayName()
                    );

                    modifiedList.add(newData);
                    needsClone = true;
                } else {
                    modifiedList.add(data);
                }
            }

            if (needsClone) {
                PacketContainer newPacket = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
                newPacket.getPlayerInfoAction().write(0, packet.getPlayerInfoAction().read(0));
                newPacket.getPlayerInfoDataLists().write(0, modifiedList);
                evt.setPacket(newPacket);
            }
        }
    }


}

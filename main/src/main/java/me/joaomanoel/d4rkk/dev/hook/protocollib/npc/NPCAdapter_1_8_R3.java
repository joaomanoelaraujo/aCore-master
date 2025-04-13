package me.joaomanoel.d4rkk.dev.hook.protocollib.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.joaomanoel.d4rkk.dev.libraries.npc.NPCLibrary;
import me.joaomanoel.d4rkk.dev.nms.npc.NpcEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NPCAdapter_1_8_R3 {

    public void onPacketSending(PacketEvent evt) {
        PacketContainer packet = evt.getPacket();

        Player player = evt.getPlayer();
        if (packet.getType() == PacketType.Play.Server.PLAYER_INFO) {
            List<PlayerInfoData> toSend = new ArrayList<>();
            boolean needsClone = false;
            for (PlayerInfoData data : packet.getPlayerInfoDataLists().read(0)) {
                NpcEntity npc = NPCLibrary.findByUUID(data.getProfile().getUUID());
                if (npc != null) {
                    if (npc.isCopySkin()) {
                        needsClone = true;
                        data.getProfile().getProperties().clear();
                        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
                        profile.getProperties().get("textures").stream().findFirst().ifPresent(prop -> data.getProfile().getProperties().put("textures", prop));
                    }
                }

                toSend.add(data);
            }

            if (!needsClone) {
                toSend.clear();
                return;
            }

            PacketContainer clone = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
            clone.getPlayerInfoAction().write(0, packet.getPlayerInfoAction().read(0));
            clone.getPlayerInfoDataLists().write(0, toSend);
            evt.setPacket(clone);
        }
    }

}

package me.joaomanoel.d4rkk.dev.hook.protocollib.fake;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.fake.FakeManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.comphenix.protocol.PacketType.Play.Server.*;

public class FakeAdapter_1_8_R3 extends PacketAdapter {

    public FakeAdapter_1_8_R3() {
        super(params().plugin(Core.getInstance()).types(
                PacketType.Play.Client.CHAT,
                TAB_COMPLETE,
                PLAYER_INFO,
                CHAT,
                SCOREBOARD_OBJECTIVE,
                SCOREBOARD_SCORE,
                SCOREBOARD_TEAM
        ));
    }

    @Override
    public void onPacketReceiving(PacketEvent evt) {
        PacketContainer packet = evt.getPacket();

        if (packet.getType() == PacketType.Play.Client.CHAT) {
            String message = packet.getStrings().read(0);

            // Se for comando, substitui os nomes dos jogadores com fake
            if (message.startsWith("/")) {
                packet.getStrings().write(0, FakeManager.replaceNickedPlayers(message, false));
            } else {
                // Se for mensagem de chat, marca para não alterar os nomes mencionados
                packet.getStrings().write(0, FakeManager.replaceNickedChanges(message));
            }
        }
    }

    @Override
    public void onPacketSending(PacketEvent evt) {
        PacketContainer packet = evt.getPacket();

        try {
            if (packet.getType() == TAB_COMPLETE) {
                handleTabComplete(packet);
            } else if (packet.getType() == PLAYER_INFO) {
                handlePlayerInfo(packet);
            } else if (packet.getType() == CHAT) {
                handleChat(packet);
            } else if (packet.getType() == SCOREBOARD_OBJECTIVE) {
                handleScoreboardObjective(packet);
            } else if (packet.getType() == SCOREBOARD_SCORE) {
                handleScoreboardScore(packet);
            } else if (packet.getType() == SCOREBOARD_TEAM) {
                handleScoreboardTeam(packet);
            }
        } catch (Exception e) {
            // Ignora erros silenciosamente para não quebrar outros packets
            Core.getInstance().getLogger().warning("Error processing packet: " + e.getMessage());
        }
    }

    /**
     * Substitui nomes no TAB completion
     */
    private void handleTabComplete(PacketContainer packet) {
        String[] completions = packet.getStringArrays().read(0);
        if (completions == null || completions.length == 0) {
            return;
        }

        List<String> list = new ArrayList<>();
        for (String complete : completions) {
            list.add(FakeManager.replaceNickedPlayers(complete, true));
        }

        packet.getStringArrays().write(0, list.toArray(new String[0]));
    }

    /**
     * Substitui o GameProfile na tab list (UUID e textura)
     */
    private void handlePlayerInfo(PacketContainer packet) {
        List<PlayerInfoData> infoDataList = packet.getPlayerInfoDataLists().read(0);
        if (infoDataList == null || infoDataList.isEmpty()) {
            return;
        }

        List<PlayerInfoData> newInfoDataList = new ArrayList<>();
        for (PlayerInfoData infoData : infoDataList) {
            WrappedGameProfile profile = infoData.getProfile();

            // Se o jogador está usando fake, clona o profile com o nome e skin fake
            if (FakeManager.isFake(profile.getName())) {
                WrappedGameProfile fakeProfile = FakeManager.cloneProfile(profile);
                infoData = new PlayerInfoData(
                        fakeProfile,
                        infoData.getLatency(),
                        infoData.getGameMode(),
                        infoData.getDisplayName()
                );
            }

            newInfoDataList.add(infoData);
        }

        packet.getPlayerInfoDataLists().write(0, newInfoDataList);
    }

    /**
     * Substitui nomes nas mensagens de chat enviadas pelo servidor
     * IMPORTANTE: Este método garante que mensagens do sistema também mostrem o fake
     */
    private void handleChat(PacketContainer packet) {
        // Processa WrappedChatComponent (formato JSON)
        WrappedChatComponent component = packet.getChatComponents().read(0);
        if (component != null) {
            String json = component.getJson();
            String replacedJson = FakeManager.replaceNickedPlayers(json, false);
            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(replacedJson));
        }

        // Processa BaseComponent[] (usado em algumas mensagens)
        Object componentsObj = packet.getModifier().read(1);
        if (componentsObj instanceof BaseComponent[]) {
            BaseComponent[] components = (BaseComponent[]) componentsObj;
            List<BaseComponent> newComps = new ArrayList<>();

            for (BaseComponent comp : components) {
                TextComponent newComp = new TextComponent("");
                String compJson = ComponentSerializer.toString(comp);
                String replacedJson = FakeManager.replaceNickedPlayers(compJson, false);

                for (BaseComponent newTextComp : ComponentSerializer.parse(replacedJson)) {
                    newComp.addExtra(newTextComp);
                }
                newComps.add(newComp);
            }

            packet.getModifier().write(1, newComps.toArray(new BaseComponent[0]));
        }
    }

    /**
     * Substitui nomes nos objetivos do scoreboard
     */
    private void handleScoreboardObjective(PacketContainer packet) {
        String objective = packet.getStrings().read(1);
        if (objective != null && !objective.isEmpty()) {
            packet.getStrings().write(1, FakeManager.replaceNickedPlayers(objective, true));
        }
    }

    /**
     * Substitui nomes nos scores do scoreboard
     */
    private void handleScoreboardScore(PacketContainer packet) {
        String scoreName = packet.getStrings().read(0);
        if (scoreName != null && !scoreName.isEmpty()) {
            packet.getStrings().write(0, FakeManager.replaceNickedPlayers(scoreName, true));
        }
    }

    /**
     * Substitui nomes nos times do scoreboard
     */
    private void handleScoreboardTeam(PacketContainer packet) {
        Object membersObj = packet.getModifier().withType(Collection.class).read(0);
        if (!(membersObj instanceof Collection)) {
            return;
        }

        @SuppressWarnings("unchecked")
        Collection<String> membersCollection = (Collection<String>) membersObj;
        List<String> members = new ArrayList<>();

        for (String member : membersCollection) {
            // Se o membro está usando fake, substitui pelo nome fake
            if (FakeManager.isFake(member)) {
                members.add(FakeManager.getFake(member));
            } else {
                members.add(member);
            }
        }

        packet.getModifier().withType(Collection.class).write(0, members);
    }
}
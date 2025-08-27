package me.joaomanoel.d4rkk.dev.cosmetic;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class GlowUtils {

    private static final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    /**
     * Ativa o glow no alvo, visível para o viewer.
     * A cor vem do time no scoreboard.
     */
    public static void applyGlow(Player target, Player viewer, ChatColor color) {
        try {
            // Scoreboard colorido
            setTeamColor(target, color, viewer);

            PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, target.getEntityId());

            List<WrappedDataValue> dataValues = new ArrayList<>();

            // Para Minecraft 1.20.6, precisamos usar o serializer correto
            WrappedDataWatcher.Serializer byteSerializer =
                    WrappedDataWatcher.Registry.get(Byte.class);

            // Index 0 é a entity flags (bit mask)
            // Bit 6 (0x40) é o glow effect
            byte entityFlags = (byte) 0x40; // Apenas o bit de glow ativo

            dataValues.add(new WrappedDataValue(0, byteSerializer, entityFlags));

            packet.getDataValueCollectionModifier().write(0, dataValues);

            manager.sendServerPacket(viewer, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove o glow do alvo para o viewer.
     */
    public static void removeGlow(Player target, Player viewer) {
        try {
            // Primeiro remove do time
            removeFromTeam(target, viewer);

            // Depois envia o packet para remover o glow
            PacketContainer packet = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, target.getEntityId());

            List<WrappedDataValue> dataValues = new ArrayList<>();

            WrappedDataWatcher.Serializer byteSerializer =
                    WrappedDataWatcher.Registry.get(Byte.class);

            // Entity flags vazias (sem glow)
            dataValues.add(new WrappedDataValue(0, byteSerializer, (byte) 0));

            packet.getDataValueCollectionModifier().write(0, dataValues);

            manager.sendServerPacket(viewer, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Helpers para scoreboard colorido =====
    private static void setTeamColor(Player target, ChatColor color, Player viewer) {
        Scoreboard scoreboard = viewer.getScoreboard();
        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            viewer.setScoreboard(scoreboard);
        }

        String teamName = "glow_" + color.name().toLowerCase();
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);

            // Métodos compatíveis com 1.8.8
            // Usamos reflection para métodos mais recentes se disponíveis
            try {
                // Tenta usar setColor() se disponível (1.13+)
                team.getClass().getMethod("setColor", ChatColor.class).invoke(team, color);
            } catch (Exception e) {
                // Na 1.8.8, usamos o prefix para a cor
                team.setPrefix(color.toString());
            }

            // Configurações para evitar conflitos (usando reflection para versões mais novas)
            try {
                // Tenta usar setOption() se disponível (1.9+)
                Class<?> optionClass = Class.forName("org.bukkit.scoreboard.Team$Option");
                Class<?> optionStatusClass = Class.forName("org.bukkit.scoreboard.Team$OptionStatus");
                Object neverOption = optionStatusClass.getField("NEVER").get(null);
                Object collisionRule = optionClass.getField("COLLISION_RULE").get(null);

                team.getClass().getMethod("setOption", optionClass, optionStatusClass)
                        .invoke(team, collisionRule, neverOption);
            } catch (Exception e) {
                // Na 1.8.8, não temos essas opções, mas não é crítico
            }
        }

        if (!team.hasEntry(target.getName())) {
            team.addEntry(target.getName());
        }
    }

    private static void removeFromTeam(Player target, Player viewer) {
        Scoreboard scoreboard = viewer.getScoreboard();
        if (scoreboard == null) return;

        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(target.getName())) {
                team.removeEntry(target.getName());
                // Se o time ficar vazio, podemos removê-lo
                if (team.getEntries().isEmpty()) {
                    team.unregister();
                }
            }
        }
    }
}
package me.joaomanoel.d4rkk.dev.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TagUtils {

    public static void setTag(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            Role otherRole = Role.getPlayerRole(player);
            setTag(online, player, otherRole.getPrefix(), getPlayerSuffix(player), otherRole.getId());
        }

        Bukkit.getScheduler().runTaskLater(Core.getInstance(), ()-> {
            for (Player online : Bukkit.getOnlinePlayers()) {
                Role otherRole = Role.getPlayerRole(online);
                setTag(player, online, otherRole.getPrefix(), getPlayerSuffix(online), otherRole.getId());
            }
        }, 3L);

    }

    public static void setTag(Player viewer, Player target, String prefix, String suffix, int sortPriority) {
        Scoreboard scoreboard = viewer.getScoreboard();
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(target.getName())) {
                team.removeEntry(target.getName());
            }
        }

        String teamName = formatTeamName(sortPriority, target.getName());
        if (teamName.length() > 16) teamName = teamName.substring(0, 16);

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        team.setPrefix(prefix);
        team.setSuffix(suffix);
        team.addEntry(target.getName());

        applyTeamColorIfSupported(team, extractColorFromPrefix(prefix));
    }

    public static void destroy(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }

    private static void applyTeamColorIfSupported(Team team, ChatColor color) {
        if (!isMinecraftVersionAtLeast(1, 13)) return;
        try {
            Method setColorMethod = team.getClass().getMethod("setColor", ChatColor.class);
            setColorMethod.setAccessible(true);
            setColorMethod.invoke(team, color);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
    }

    private static boolean isMinecraftVersionAtLeast(int major, int minor) {
        try {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            int versionMinor = protocolManager.getMinecraftVersion().getMinor();
            int versionMajor = protocolManager.getMinecraftVersion().getMajor();
            return versionMajor > major || (versionMajor == major && versionMinor >= minor);
        } catch (Exception e) {
            return false;
        }
    }

    private static String formatTeamName(int priority, String playerName) {
        return String.format("%03d_%s", priority, playerName.toLowerCase().replaceAll("[^a-z0-9_]", ""));
    }

    public static String getPlayerSuffix(Player player) {
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null && !Manager.isFake(player.getName())) {
            DataContainer container = profile.getDataContainer("aCoreProfile", "clan");
            if (container != null) {
                String suffix = " " + container.getAsString().replace(" ", "");
                if (!suffix.contains("§8")) {
                    return suffix;
                }
            }
        }
        return "";
    }

    private static ChatColor extractColorFromPrefix(String prefix) {
        for (int i = 0; i < prefix.length() - 1; i++) {
            if (prefix.charAt(i) == '§') {
                char code = prefix.charAt(i + 1);
                ChatColor color = ChatColor.getByChar(code);
                if (color != null && color.isColor()) {
                    return color;
                }
            }
        }
        return ChatColor.WHITE;
    }
}

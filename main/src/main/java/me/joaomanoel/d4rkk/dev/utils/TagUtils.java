package me.joaomanoel.d4rkk.dev.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.cosmetic.types.ColoredTag;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.fake.FakeManager;
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
        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) return;

        Role tagRole = Role.getPlayerTagRole(player);
        if (tagRole == null) {
            tagRole = Role.getPlayerRole(player);
        }

        if (tagRole == null) return;

        for (Player online : Bukkit.getOnlinePlayers()) {
            setTag(online, player, tagRole.getPrefix(), getPlayerSuffix(player), tagRole.getId());
        }

        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
            for (Player online : Bukkit.getOnlinePlayers()) {
                Role onlineTagRole = Role.getPlayerTagRole(online);
                if (onlineTagRole == null) {
                    onlineTagRole = Role.getPlayerRole(online);
                }

                if (onlineTagRole == null) continue;

                setTag(player, online, onlineTagRole.getPrefix(), getPlayerSuffix(online), onlineTagRole.getId());
            }
        }, 3L);
    }

    private static String getSelectedTagColor(Player player) {
        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) return null;

        if (ProfileUtils.isInMatch(profile)) {
            return null;
        }

        SelectedContainer selected = profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class);
        if (selected == null) return null;

        ColoredTag coloredTag = selected.getSelected(CosmeticType.COLORED_TAG, ColoredTag.class);

        if (coloredTag != null && coloredTag.getId() != 0) {
            return coloredTag.getColor();
        }
        return null;
    }

    public static void setTag(Player viewer, Player target, String prefix, String suffix, int sortPriority) {
        if (prefix == null || prefix.isEmpty()) {
            prefix = "§7";
        }

        if (suffix == null) {
            suffix = "";
        }

        Scoreboard scoreboard = viewer.getScoreboard();
        String finalName = getFinalName(target.getName());

        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(finalName)) {
                team.removeEntry(finalName);
            }
        }

        String teamName = formatTeamName(sortPriority, finalName);
        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        String tagColor = getSelectedTagColor(target);
        String finalPrefix;

        if (tagColor != null && !tagColor.isEmpty()) {
            finalPrefix = tagColor + ChatColor.stripColor(prefix);
            ChatColor colorCode = ChatColor.getByChar(tagColor.replace("§", "").charAt(0));
            if (colorCode != null) {
                applyTeamColorIfSupported(team, colorCode);
            }
        } else {
            finalPrefix = prefix;
            ChatColor extractedColor = extractColorFromPrefix(prefix);
            if (extractedColor != null) {
                applyTeamColorIfSupported(team, extractedColor);
            }
        }

        team.setPrefix(finalPrefix);
        team.setSuffix(suffix);
        team.addEntry(finalName);
    }

    public static void destroy(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        String finalName = getFinalName(player.getName());

        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(finalName)) {
                team.removeEntry(finalName);
            }
        }
    }

    private static void applyTeamColorIfSupported(Team team, ChatColor color) {
        if (color == null) return;
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
        if (prefix == null || prefix.isEmpty()) {
            return ChatColor.WHITE;
        }

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

    private static String getFinalName(String target) {
        return FakeManager.isFake(target) ? FakeManager.getFake(target) : target;
    }
}

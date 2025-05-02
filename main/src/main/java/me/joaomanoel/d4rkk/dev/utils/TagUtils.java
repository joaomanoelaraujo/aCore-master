package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;


@SuppressWarnings("deprecation")
public class TagUtils {

    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    public static void setTag(Player player) {
        Role role = Role.getPlayerRole(player);
        String suffix = "";
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
            DataContainer container = profile.getDataContainer("aCoreProfile", "clan");
            if (container != null) {
                suffix = " " + container.getAsString().replace(" ", "");
                if (suffix.contains("§8")) {
                    suffix = "";
                }
            }
        }

        setTag(player, role.getName(), role.getPrefix(), suffix, role.getId());
    }

    public static void setTag(Player player, Role grupo) {
        String suffix = "";
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null && !Manager.isFake(player.getName())) {
            DataContainer container = profile.getDataContainer("aCoreProfile", "clan");
            if (container != null) {
                suffix = " " + container.getAsString().replace(" ", "");
                if (suffix.contains("§8")) {
                    suffix = "";
                }
            }
        }

        setTag(player, grupo.getName(), grupo.getPrefix(), suffix, grupo.getId());

    }


    public static void setTag(Player player, String tagName, String prefix, String suffix, int sortPriority) {
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }

        String teamName = formatTeamName(tagName, sortPriority);
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }

        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);
    }

    public static void clear(Player player) {
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }

    private static String formatTeamName(String prefix, int sortedPriority) {
        return String.format("%03d_%s", sortedPriority, prefix);
    }
}
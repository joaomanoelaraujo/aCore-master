package me.joaomanoel.d4rkk.dev.api;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;

public class StatsAPI {

    public String getLevel(Profile profile) {
        String level = null;
        if (profile != null) {
            switch (Core.minigame) {
                case "Sky Wars":
                    level = String.valueOf(profile.getStats("aCoreSkyWars", "level"));
                    break;
                case "Bed Wars":
                    level = String.valueOf(profile.getStats("aCoreBedWars", "level"));
                    break;
                case "The Bridge":
                    level = String.valueOf(profile.getStats("aCoreTheBridge", "level"));
                    break;
            }

            return StringUtils.formatNumber(Long.parseLong(level)) + " §7(" + Core.minigame + ")";
        }
        return "";
    }

    public String getGuild() {
        return "Nothing";
    }

}

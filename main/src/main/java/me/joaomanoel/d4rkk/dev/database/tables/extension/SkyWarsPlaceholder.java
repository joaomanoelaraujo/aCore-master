package me.joaomanoel.d4rkk.dev.database.tables.extension;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SkyWarsPlaceholder extends PlaceholderExpansion {
    
    @Override
    public @NotNull String getIdentifier() {
        return "skywars";
    }

    @Override
    public @NotNull String getAuthor() {
        return "d4rkk";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // para manter a expansão após reinícios
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, String params) {
        if (player == null) {
            return "";
        }

        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
            return "";
        }

        String table = "aCoreSkyWars";
        if (params.startsWith("SkyWars_")) {
            String value = params.replace("SkyWars_", "");
            if (value.equals("kills") || value.equals("deaths") || value.equals("assists") || value.equals("games") || value.equals("wins")) {
                return StringUtils.formatNumber(profile.getStats(table, "insane" + value, "insane2v2" + value, "normal" + value, "normal2v2" + value));
            } else if (value.equals("insanekills") || value.equals("insanedeaths") || value.equals("insaneassists") || value.equals("insanegames") || value.equals("insanewins")) {
                return StringUtils.formatNumber(profile.getStats(table, value));
            } else if (value.equals("insane2v2kills") || value.equals("insane2v2deaths") || value.equals("insane2v2assists") || value.equals("insane2v2games") || value.equals("insane2v2wins")) {
                return StringUtils.formatNumber(profile.getStats(table, value));
            } else if (value.equals("normal2v2kills") || value.equals("normal2v2deaths") || value.equals("normal2v2assists") || value.equals("normal2v2games") || value.equals("normal2v2wins")) {
                return StringUtils.formatNumber(profile.getStats(table, value));
            } else if (value.equals("normalkills") || value.equals("normaldeaths") || value.equals("normalassists") || value.equals("normalgames") || value.equals("normalwins")) {
                return StringUtils.formatNumber(profile.getStats(table, value));
            } else if (value.equals("coins")) {
                return StringUtils.formatNumber(profile.getCoins(table));
            }
        }

        return null; // Retorna null se o placeholder não for reconhecido
    }
}

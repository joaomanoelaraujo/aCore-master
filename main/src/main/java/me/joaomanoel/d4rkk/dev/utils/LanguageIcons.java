package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.Core;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageIcons {
    private static File file;
    private static YamlConfiguration config;
    
    public static void load(Core plugin) {
        file = new File(plugin.getDataFolder(), "languages.yml");
        if (!file.exists()) {
            plugin.saveResource("languages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static String getIcon(String langKey) {
        if (config == null || !config.contains("languages")) {
            return null;
        } for (String key : config.getConfigurationSection("languages").getKeys(false)) {
            if (key.equalsIgnoreCase(langKey)) {
                String icon = config.getString("languages." + key + ".icon");
                return icon != null ? StringUtils.formatColors(icon) : null;
            }
        } return null;
    }
}

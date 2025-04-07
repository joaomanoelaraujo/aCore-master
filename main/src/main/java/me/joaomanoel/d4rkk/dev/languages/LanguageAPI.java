package me.joaomanoel.d4rkk.dev.languages;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageAPI {

    private static final Map<String, KConfig> CACHED_CONFIG = new HashMap<>();
    private static final String defaultLanguage = Core.getInstance().getConfig().getString("defaultLanguage");

    public static void setupLanguages(String... defaultLanguages) throws IOException {
        File folder = new File("plugins/" + Core.getInstance().getDescription().getName() + "/translate");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String name;
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            name = file.getName().replace(".yml", "");
            CACHED_CONFIG.put(name, KConfig.getConfig(Core.getInstance(), file.getPath().replace("\\" + file.getName(), ""), name));
        }

        for (String defaultLanguage : Arrays.stream(defaultLanguages).filter(fileName -> !CACHED_CONFIG.containsKey(fileName)).collect(Collectors.toList())) {
            File file = new File("plugins/" + Core.getInstance().getDescription().getName() + "/translate/" + defaultLanguage + ".yml");
            Files.copy(Objects.requireNonNull(LanguageAPI.class.getResourceAsStream("/" + defaultLanguage + ".yml")), file.toPath());
            CACHED_CONFIG.put(defaultLanguage, KConfig.getConfig(Core.getInstance(), file.getPath().replace("\\" + file.getName(), ""), file.getName().replace(".yml", "")));
        }

        if (getConfig() == null) {
            throw new RuntimeException("Default language not found");
        }

        Core.getInstance().getLogger().info("Todas as linguagens foram carregadas com sucesso!");
    }

    public static List<String> listAllKeys() {
        return new ArrayList<>(CACHED_CONFIG.keySet());
    }

    public static KConfig getConfig(Profile profile) {
        String lang = profile.getLanguageContainer().getLanguage();
        if (!CACHED_CONFIG.containsKey(lang)) {
            return getConfig();
        }

        return CACHED_CONFIG.get(lang);
    }

    public static KConfig getConfig() {
        return CACHED_CONFIG.getOrDefault(defaultLanguage, null);
    }

    public static String getDefaultConfigName() {
        return defaultLanguage;
    }
    
}

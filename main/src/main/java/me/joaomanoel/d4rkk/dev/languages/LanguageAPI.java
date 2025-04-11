package me.joaomanoel.d4rkk.dev.languages;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageAPI {

    private static final Map<String, ColorTranslatingConfig> CACHED_CONFIG = new HashMap<>();
    private static final String defaultLanguage = Core.getInstance().getConfig().getString("defaultLanguage");

    public static void setupLanguages(String... defaultLanguages) throws IOException {
        File folder = new File("plugins/" +
                Core.getInstance().getDescription().getName() +
                "/translate");
        if (!folder.exists()) {
            folder.mkdirs();
        }


        for (File file : Objects.requireNonNull(folder.listFiles((d, name) -> name.endsWith(".yml")))) {
            String name = file.getName().replace(".yml", "");
            String folderPath = file.getParent();
            KConfig config = KConfig.getConfig(Core.getInstance(), folderPath, name);
            CACHED_CONFIG.put(name, new ColorTranslatingConfig(config));
        }

        for (String lang : Arrays.stream(defaultLanguages)
                .filter(l -> !CACHED_CONFIG.containsKey(l))
                .collect(Collectors.toList())) {
            File dest = new File(folder, lang + ".yml");
            Files.copy(Objects.requireNonNull(
                            LanguageAPI.class.getResourceAsStream("/" + lang + ".yml")),
                    dest.toPath());

            String folderPath = dest.getParent();
            String name = dest.getName().replace(".yml", "");
            KConfig config = KConfig.getConfig(Core.getInstance(), folderPath, name);
            CACHED_CONFIG.put(name, new ColorTranslatingConfig(config));
        }

        if (getConfig() == null) {
            throw new RuntimeException("Default language not found");
        }

        Core.getInstance().getLogger().info("All languages loaded successfully!");
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

    private static class ColorTranslatingConfig extends KConfig {
        private final KConfig delegate;

        public ColorTranslatingConfig(KConfig delegate) {
            super(Core.getInstance(), delegate.getFile().getParent(), delegate.getFile().getName().replace(".yml", ""));
            this.delegate = delegate;
        }

        @Override
        public String getString(String path) {
            String value = delegate.getString(path);
            if (value != null) {
                value = value.replace("\\n", "\n");
                return StringUtils.formatColors(value);
            }
            return null;
        }


        @Override
        public List<String> getStringList(String path) {
            List<String> list = delegate.getStringList(path);
            return list != null ? list.stream()
                    .map(StringUtils::formatColors)
                    .collect(Collectors.toList()) : null;
        }

        @Override
        public boolean createSection(String path) {
            return delegate.createSection(path);
        }

        @Override
        public boolean set(String path, Object obj) {
            return delegate.set(path, obj);
        }

        @Override
        public boolean contains(String path) {
            return delegate.contains(path);
        }

        @Override
        public Object get(String path) {
            return delegate.get(path);
        }

        @Override
        public int getInt(String path) {
            return delegate.getInt(path);
        }

        @Override
        public int getInt(String path, int def) {
            return delegate.getInt(path, def);
        }

        @Override
        public double getDouble(String path) {
            return delegate.getDouble(path);
        }

        @Override
        public double getDouble(String path, double def) {
            return delegate.getDouble(path, def);
        }

        @Override
        public boolean getBoolean(String path) {
            return delegate.getBoolean(path);
        }

        @Override
        public boolean getBoolean(String path, boolean def) {
            return delegate.getBoolean(path, def);
        }

        @Override
        public Set<String> getKeys(boolean flag) {
            return delegate.getKeys(flag);
        }

        @Override
        public ConfigurationSection getSection(String path) {
            return delegate.getSection(path);
        }

        @Override
        public void reload() {
            delegate.reload();
        }

        @Override
        public boolean save() {
            return delegate.save();
        }

        @Override
        public File getFile() {
            return delegate.getFile();
        }

        @Override
        public YamlConfiguration getRawConfig() {
            return delegate.getRawConfig();
        }
    }
}

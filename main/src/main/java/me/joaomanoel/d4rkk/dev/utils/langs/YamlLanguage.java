package me.joaomanoel.d4rkk.dev.utils.langs;

import java.util.Map;

public class YamlLanguage implements Language {
    private final String name;
    private final Map<String, String> translations;

    public YamlLanguage(String name, Map<String, String> translations) {
        this.name = name;
        this.translations = translations;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTranslation(String key) {
        return translations.getOrDefault(key, "§cTranslation missing for: " + key);
    }
}

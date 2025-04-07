package me.joaomanoel.d4rkk.dev.cosmetic;

import me.joaomanoel.d4rkk.dev.languages.LanguageAPI;

public enum CosmeticType {

    JOIN_MESSAGE(LanguageAPI.getConfig().getString("cosmetic.join_message_name")),
    MVPCOLOR(LanguageAPI.getConfig().getString("cosmetic.rank")),
    PUNCH(LanguageAPI.getConfig().getString("cosmetic.punch")),
    COLORED_TAG(LanguageAPI.getConfig().getString("cosmetic.coloredtag_name"));

    private final String[] names;

    CosmeticType(String... names) {
        this.names = names;
    }
    public String getName(long index) {
        return this.names[(int) (index - 1)];
    }
}

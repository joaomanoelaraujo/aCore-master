package me.joaomanoel.d4rkk.dev.cosmetic;

import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;

public enum CosmeticType {

    JOIN_MESSAGE(EN_US.cosmetic$join_message_name),
    LANGUAGE("Languages"),
    MVPCOLOR("Rank Color"),
    PUNCH("Punch"),
    COLORED_TAG(EN_US.cosmetic$coloredtag_name);

    private final String[] names;

    CosmeticType(String... names) {
        this.names = names;
    }
    public String getName(long index) {
        return this.names[(int) (index - 1)];
    }
}

package me.joaomanoel.d4rkk.dev.player.enums;

public enum MatchMaking {
    ATIVADO,
    DESATIVADO;

    private static final MatchMaking[] VALUES = values();

    public static MatchMaking getByOrdinal(long ordinal) {
        if (ordinal < 2 && ordinal > -1) {
            return VALUES[(int) ordinal];
        }

        return null;
    }

    public String getInkSack() {
        if (this == ATIVADO) {
            return "10";
        }

        return "1";
    }

    public boolean isEnabled() {
        return this == ATIVADO;
    }

    public String getName() {
        if (this == ATIVADO) {
            return "§aON";
        }

        return "§cOFF";
    }

    public MatchMaking next() {
        if (this == DESATIVADO) {
            return ATIVADO;
        }

        return DESATIVADO;
    }
}

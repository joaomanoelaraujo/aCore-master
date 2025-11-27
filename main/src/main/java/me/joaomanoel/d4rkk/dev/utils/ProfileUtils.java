package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.game.GameState;
import me.joaomanoel.d4rkk.dev.player.Profile;

public class ProfileUtils {

    public static boolean isInMatch(Profile profile) {
        if (profile == null) return false;

        Game<?> game = profile.getGame();
        if (game == null) return false;

        return game.getState() == GameState.EMJOGO;
    }
}

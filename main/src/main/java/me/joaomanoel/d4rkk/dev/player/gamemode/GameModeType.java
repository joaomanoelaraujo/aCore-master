package me.joaomanoel.d4rkk.dev.player.gamemode;

import java.util.Arrays;
import java.util.List;

public enum GameModeType {
   CREATIVE(Arrays.asList("criativo", "1", "creative")),
   SURVIVAL(Arrays.asList("sobrevivência", "0", "survival")),
   ADVENTURE(Arrays.asList("aventura", "2", "adventure")),
   SPECTATOR(Arrays.asList("espectador", "3", "spectator"));

   public static final GameModeType[] VALUES = values();
   public final List<String> names;

   GameModeType(List<String> names) {
      this.names = names;
   }

   public List<String> getNames() {
      return this.names;
   }

   public static GameModeType fromName(String name) {
      for (GameModeType type : VALUES) {
         if (type.getNames().contains(name.toLowerCase())) {
            return type;
         }
      }

      return null;
   }

   private static GameModeType[] getValues() {
      return new GameModeType[]{CREATIVE, SURVIVAL, ADVENTURE, SPECTATOR};
   }
}

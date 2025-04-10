package me.joaomanoel.d4rkk.dev.game;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public enum GameState {
  AGUARDANDO, INICIANDO, EMJOGO, ENCERRADO;

  private static Map<String, String> translations;


  public static void loadLanguage(FileConfiguration config) {
    String language = config.getString("defaultLanguage", "PT_BR");
    translations = getTranslationsForLanguage(language);
  }

  private static Map<String, String> getTranslationsForLanguage(String language) {
    Map<String, String> translationMap = new HashMap<>();
    switch (language) {
      case "EN_US":
        translationMap.put("AGUARDANDO", "Waiting for players...");
        translationMap.put("INICIANDO", "Starting game...");
        translationMap.put("EMJOGO", "Game in progress...");
        translationMap.put("ENCERRADO", "Game over!");
        break;
      case "ES_ES":
        translationMap.put("AGUARDANDO", "Esperando jugadores...");
        translationMap.put("INICIANDO", "Comenzando el juego...");
        translationMap.put("EMJOGO", "Juego en progreso...");
        translationMap.put("ENCERRADO", "¡Juego terminado!");
        break;
      case "JA_JP":
        translationMap.put("AGUARDANDO", "プレイヤーを待っています...");
        translationMap.put("INICIANDO", "ゲームを開始します...");
        translationMap.put("EMJOGO", "ゲーム進行中...");
        translationMap.put("ENCERRADO", "ゲーム終了！");
        break;
      case "ZH_CN":
        translationMap.put("AGUARDANDO", "等待 ...");
        translationMap.put("INICIANDO", "游戏开始...");
        translationMap.put("EMJOGO", "游戏进行中...");
        translationMap.put("ENCERRADO", "游戏结束！");
        break;
      case "PT_BR":
      default:
        translationMap.put("AGUARDANDO", "Aguardando jogadores...");
        translationMap.put("INICIANDO", "O jogo está começando...");
        translationMap.put("EMJOGO", "Jogo em progresso...");
        translationMap.put("ENCERRADO", "Jogo encerrado!");
        break;
    }
    return translationMap;
  }

  // Método para obter a tradução do estado
  public String getTranslation() {
    return translations.get(this.name());
  }

  // Método para verificar se o jogador pode entrar no jogo
  public boolean canJoin() {
    return this == AGUARDANDO;
  }
}

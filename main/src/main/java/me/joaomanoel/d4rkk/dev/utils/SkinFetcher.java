package me.joaomanoel.d4rkk.dev.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SkinFetcher {

  private static final String MOJANG_API_UUID = "https://api.mojang.com/users/profiles/minecraft/%s";
  private static final String MOJANG_API_PROFILE = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
  private static final int TIMEOUT = 5000;
  private static final Logger LOGGER = Logger.getLogger("SkinFetcher");
  private static final JsonParser JSON_PARSER = new JsonParser();

  public static CompletableFuture<String> fetchSkin(String playerName) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        String uuid = getUUID(playerName);
        if (uuid == null) {
          return null;
        }

        return getSkinTexture(uuid);
      } catch (Exception e) {
        LOGGER.log(Level.WARNING, "Erro ao buscar skin de " + playerName + ": " + e.getMessage());
        return null;
      }
    });
  }

  private static String getUUID(String playerName) {
    HttpURLConnection connection = null;
    try {
      URL url = new URL(String.format(MOJANG_API_UUID, playerName));
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(TIMEOUT);
      connection.setReadTimeout(TIMEOUT);
      connection.setRequestProperty("User-Agent", "kCore/1.0");

      int responseCode = connection.getResponseCode();
      if (responseCode != 200) {
        return null;
      }

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }

        JsonObject json = JSON_PARSER.parse(response.toString()).getAsJsonObject();
        return json.get("id").getAsString();
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao buscar UUID de " + playerName);
      return null;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private static String getSkinTexture(String uuid) {
    HttpURLConnection connection = null;
    try {
      URL url = new URL(String.format(MOJANG_API_PROFILE, uuid));
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(TIMEOUT);
      connection.setReadTimeout(TIMEOUT);
      connection.setRequestProperty("User-Agent", "aCore/1.0");

      int responseCode = connection.getResponseCode();
      if (responseCode != 200) {
        return null;
      }

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }

        JsonObject json = JSON_PARSER.parse(response.toString()).getAsJsonObject();
        if (!json.has("properties")) {
          return null;
        }

        JsonObject properties = json.getAsJsonArray("properties").get(0).getAsJsonObject();
        String value = properties.get("value").getAsString();
        String signature = properties.get("signature").getAsString();

        return value + ":" + signature;
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Erro ao buscar textura da skin: " + e.getMessage());
      return null;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public static String formatUUID(String uuidWithoutDashes) {
    if (uuidWithoutDashes == null || uuidWithoutDashes.length() != 32) {
      return uuidWithoutDashes;
    }

    return uuidWithoutDashes.substring(0, 8) + "-" +
            uuidWithoutDashes.substring(8, 12) + "-" +
            uuidWithoutDashes.substring(12, 16) + "-" +
            uuidWithoutDashes.substring(16, 20) + "-" +
            uuidWithoutDashes.substring(20);
  }
}

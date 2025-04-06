package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.plugin.KPlugin;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class aUpdater {

  public static aUpdater UPDATER;
  public boolean canDownload;
  private final KPlugin plugin;
  private final KLogger logger;
  private final int resourceId;

  public aUpdater(KPlugin plugin, int resourceId) {
    this.plugin = plugin;
    this.logger = ((KLogger) this.plugin.getLogger()).getModule("UPDATER");
    this.resourceId = resourceId;
  }

  public static String getVersion(int resourceId) {
    try {
      HttpsURLConnection connection = (HttpsURLConnection) new URL("https://aetherplugins.com/api/v1/plugin/" + resourceId).openConnection();
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      JSONObject object = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
      return ((JSONObject) object.get("version")).get("id").toString();
    } catch (Exception ex) {
      return null;
    }
  }

  public void run() {
    this.logger.info("Searching for updates...");
    String latest = getVersion(this.resourceId);
    String current = this.plugin.getDescription().getVersion();
    if (latest == null) {
      logger.severe("Failed to check for updates.");
    } else {
      if (current.equals(latest)) {
        this.logger.info("Plugin is up to date.");
      } else {
        this.logger.warning("Update found. Use /ac update to proceed.");
        UPDATER = this;
        this.canDownload = true;
      }
    }
  }



  /**
   * Gera uma barra de progresso manualmente (compatível com Java 8).
   */
  private String generateProgressBar(int filled, int total) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < filled; i++) sb.append("#");  // Parte preenchida
    for (int i = filled; i < total; i++) sb.append("-"); // Parte vazia
    return sb.toString();
  }

  public void downloadUpdate(Player player) {
    player.sendMessage("§aAttempting to download update...");

    try {
      File file = new File("plugins/aCore/update", "aCore.jar");
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      HttpsURLConnection connection = (HttpsURLConnection) new URL("https://aetherplugins.com/download/aCore.jar").openConnection();
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      int max = connection.getContentLength();
      BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
      FileOutputStream fos = new FileOutputStream(file);
      BufferedOutputStream bout = new BufferedOutputStream(fos, 7024);

      byte[] buffer = new byte[1024]; // 1KB buffer
      int bytesRead;
      int totalBytesRead = 0;
      player.sendMessage("§aFile size: " + (max / 1024) + "KB");
      while ((bytesRead = in.read(buffer)) != -1) {
        bout.write(buffer, 0, bytesRead);
        totalBytesRead += bytesRead;
        int percentage = (totalBytesRead * 100) / max;
        NMS.sendActionBar(player, "§fDownloading " + file.getName() + " §7[§a" + StringUtils.repeat("█", percentage / 4) + "§8" + StringUtils.repeat("█", 25 - (percentage / 4)) + "§7]");
      }

      NMS.sendActionBar(player, "§aUpdate downloaded, stop the server to proceed.");
      in.close();
      bout.close();
    } catch (Exception ex) {
      NMS.sendActionBar(player, "§aFailed to download update: " + ex.getMessage());
    }
  }
}

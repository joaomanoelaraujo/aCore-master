/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;

import java.io.File;

import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    public static File sqlFile = new File(Core.getInstance().getDataFolder(), "config.yml");
    public static FileConfiguration sqlCfg = YamlConfiguration.loadConfiguration(sqlFile);
    public static File file = new File(Core.getInstance().getDataFolder(), "config.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    public static int MAX_LENGTH;
    public static int CLEANUP_REPLAYS;
    public static boolean RECORD_BLOCKS;
    public static boolean REAL_CHANGES;
    public static boolean RECORD_ITEMS;
    public static boolean RECORD_ENTITIES;
    public static boolean RECORD_CHAT;
    public static boolean SAVE_STOP;
    public static boolean RECORD_STARTUP;
    public static boolean USE_OFFLINE_SKINS;
    public static boolean HIDE_PLAYERS;
    public static boolean UPDATE_NOTIFY;
    public static boolean USE_DATABASE;
    public static boolean ADD_PLAYERS;
    public static boolean WORLD_RESET;
    public static MySQLDatabase mySQLDatabase;
    public static ReplayQuality QUALITY;
    public static String DEATH_MESSAGE;
    public static String LEAVE_MESSAGE;
    public static String CHAT_FORMAT;
    public static String JOIN_MESSAGE;

    public static void loadConfigs() {
        ItemConfig.loadConfig();
        ConfigManager.loadData(true);
    }

    public static void loadData(boolean initial) {
        MAX_LENGTH = 3600;
        SAVE_STOP = false;
        RECORD_STARTUP = false;
        USE_OFFLINE_SKINS = true;
        QUALITY = ReplayQuality.HIGH;
        HIDE_PLAYERS = false;
        CLEANUP_REPLAYS = -1;
        ADD_PLAYERS = true;
        UPDATE_NOTIFY = false;
        if (initial) {
            USE_DATABASE = true;
        }
        DEATH_MESSAGE = "Morreu sozinho";
        LEAVE_MESSAGE = "Saiu do servidor";
        JOIN_MESSAGE = "Entrou no servidor";
        WORLD_RESET = true;
        CHAT_FORMAT = "null";
        RECORD_BLOCKS = true;
        REAL_CHANGES = true;
        RECORD_ITEMS = true;
        RECORD_ENTITIES = true;
        RECORD_CHAT = false;
        if (USE_DATABASE) {
            KConfig config = KConfig.getConfig(Core.getInstance(), "plugins/aCore", "config");
            String host = config.getString("database.mysql.host");
            int port = config.getInt("database.mysql.port", 3306);
            String username = config.getString("database.mysql.user");
            String database = config.getString("database.mysql.name");
            String password = config.getString("database.mysql.pass");
            String prefix = "";
            MySQLDatabase mysql = new MySQLDatabase(host, port, database, username, password, prefix);
            DatabaseRegistry.registerDatabase(mysql);
            DatabaseRegistry.getDatabase().getService().createReplayTable();
            mySQLDatabase = mysql;
        }
        ItemConfig.loadData();
    }

    public static void reloadConfig() {
        try {
            cfg.load(file);
            ItemConfig.cfg.load(ItemConfig.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        ConfigManager.loadData(false);
    }

    public static MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
    }

    static {
        QUALITY = ReplayQuality.HIGH;
    }
}


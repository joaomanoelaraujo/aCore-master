package me.joaomanoel.d4rkk.dev.bungee.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper para logging no BungeeCord
 * Substitui a dependência do PluginLogger do Bukkit
 */
public class BungeeLogger {
    
    private final Logger logger;
    private final String module;
    
    public BungeeLogger(Logger logger, String module) {
        this.logger = logger;
        this.module = module;
    }
    
    public BungeeLogger getModule(String moduleName) {
        return new BungeeLogger(this.logger, moduleName);
    }
    
    public void info(String message) {
        logger.info("[" + module + "] " + message);
    }
    
    public void warning(String message) {
        logger.warning("[" + module + "] " + message);
    }
    
    public void severe(String message) {
        logger.severe("[" + module + "] " + message);
    }
    
    public void log(Level level, String message) {
        logger.log(level, "[" + module + "] " + message);
    }
    
    public void log(Level level, String message, Throwable throwable) {
        logger.log(level, "[" + module + "] " + message, throwable);
    }
    
    public void fine(String message) {
        logger.fine("[" + module + "] " + message);
    }
    
    public void config(String message) {
        logger.config("[" + module + "] " + message);
    }
}
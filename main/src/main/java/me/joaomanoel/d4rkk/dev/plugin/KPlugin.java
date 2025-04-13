package me.joaomanoel.d4rkk.dev.plugin;

import me.joaomanoel.d4rkk.dev.plugin.config.FileUtils;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;
import me.joaomanoel.d4rkk.dev.plugin.config.KWriter;
import me.joaomanoel.d4rkk.dev.plugin.logger.KLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;

public abstract class KPlugin extends JavaPlugin {

  private final FileUtils fileUtils;

  public KPlugin() {
    this.fileUtils = new FileUtils(this);
    Class<?> clazz = JavaPlugin.class;
    try {
      Field field = clazz.getDeclaredField("logger");
      field.setAccessible(true);
      field.set(this, new KLogger(this));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    
    this.start();
  }
  
  public abstract void start();
  
  public abstract void load();
  
  public abstract void enable();
  
  public abstract void disable();
  
  @Override
  public void onLoad() {
    this.load();
  }

  @Override
  public void onEnable() {
    this.enable();
  }

  @Override
  public void onDisable() {
    this.disable();
  }
  
  public KConfig getConfig(String name) {
    return this.getConfig("", name);
  }
  
  public KConfig getConfig(String path, String name) {
    return KConfig.getConfig(this, "plugins/" + this.getName() + "/" + path, name);
  }
  
  public KWriter getWriter(File file) {
    return this.getWriter(file, "");
  }
  
  public KWriter getWriter(File file, String header) {
    return new KWriter((KLogger) this.getLogger(), file, header);
  }
  
  public FileUtils getFileUtils() {
    return this.fileUtils;
  }
}

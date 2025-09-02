package me.joaomanoel.d4rkk.dev.bungee.methods;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BungeeWriter {
    private final File file;
    private final Configuration config;

    public BungeeWriter(File file, Configuration config) {
        this.file = file;
        this.config = config;
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public void write() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

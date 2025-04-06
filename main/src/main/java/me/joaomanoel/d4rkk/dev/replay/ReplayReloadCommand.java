/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReplayReloadCommand
extends SubCommand {
    public ReplayReloadCommand(AbstractCommand parent) {
        super(parent, "reload", "Reloads the config", "reload", false);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        ConfigManager.reloadConfig();
        cs.sendMessage("§aSuccessfully reloaded the configuration.");
        return true;
    }
}


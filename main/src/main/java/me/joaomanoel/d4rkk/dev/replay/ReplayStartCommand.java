/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReplayStartCommand
extends SubCommand {
    public ReplayStartCommand(AbstractCommand parent) {
        super(parent, "start", "Records a new replay", "start [Name][:Duration] [<Players ...>]", false);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        final String name = this.parseName(args);
        int duration = this.parseDuration(args);
        if (name == null || duration < 0) {
            return false;
        }
        if (name.length() > 40) {
            cs.sendMessage("§ecReplay name is too long.");
            return true;
        }
        if (ReplayManager.activeReplays.containsKey(name)) {
            cs.sendMessage("§ecReplay already exists.");
            return true;
        }
        ArrayList<Player> toRecord = new ArrayList<>();
        if (args.length <= 2) {
            toRecord.addAll(Bukkit.getOnlinePlayers());
        } else {
            for (int i = 2; i < args.length; ++i) {
                if (Bukkit.getPlayer(args[i]) == null) continue;
                toRecord.add(Bukkit.getPlayer(args[i]));
            }
        }
        ReplayAPI.getInstance().recordReplay(name, toRecord);
        if (duration == 0) {
            cs.sendMessage("§eaSuccessfully started recording §e" + name + "§e7.\n§e7Use §e6/Replay stop " + name + "§e7 to save it.");
        } else {
            cs.sendMessage("§eaSuccessfully started recording §e" + name + "§e7.\n§e7The Replay will be saved after §e6" + duration + "§e7 seconds");
            new BukkitRunnable(){

                public void run() {
                    ReplayAPI.getInstance().stopReplay(name, true, true);
                }
            }.runTaskLater(Core.getInstance(), duration * 20L);
        }
        if (args.length <= 2) {
            cs.sendMessage("§e7INFO: You are recording all online players.");
        }
        return true;
    }

    private String parseName(String[] args) {
        if (args.length >= 2) {
            String[] split = args[1].split(":");
            if (args[1].contains(":")) {
                if (split.length == 2 && !split[0].isEmpty()) {
                    return split[0];
                }
            } else {
                return args[1];
            }
        }
        return StringUtils.getRandomString(6);
    }

    private int parseDuration(String[] args) {
        if (args.length < 2 || !args[1].contains(":")) {
            return 0;
        }
        String[] split = args[1].split(":");
        if (split.length == 2 && MathUtils.isInt(split[1])) {
            return Integer.parseInt(split[1]);
        }
        if (split.length == 1) {
            if (!split[0].startsWith(":") || !MathUtils.isInt(split[0])) {
                return -1;
            }
            return Integer.parseInt(split[0]);
        }
        return 0;
    }
}


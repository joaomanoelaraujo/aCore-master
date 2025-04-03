/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.joaomanoel.d4rkk.dev.replay;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplayLeaveCommand
extends SubCommand {
    public ReplayLeaveCommand(AbstractCommand parent) {
        super(parent, "leave", "Leave your Replay", "leave", true);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        Player p = (Player)cs;
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
            replayer.stop();
        } else {
            p.sendMessage("§cYou need to play a Replay first");
        }
        return true;
    }
}


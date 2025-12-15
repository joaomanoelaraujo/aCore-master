/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.replay.AbstractCommand;
import me.joaomanoel.d4rkk.dev.replay.DefaultReplaySaver;
import me.joaomanoel.d4rkk.dev.replay.ReplaySaver;
import me.joaomanoel.d4rkk.dev.replay.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReplayReformatCommand
extends SubCommand {
    public ReplayReformatCommand(AbstractCommand parent) {
        super(parent, "reformat", "Reformat the replays", "reformat", false);
        this.setEnabled(false);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        cs.sendMessage("Reformatting Replay files...");
        ((DefaultReplaySaver)ReplaySaver.replaySaver).reformatAll();
        cs.sendMessage("§aFinished. Check console for details.");
        return true;
    }
}


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

public class ReplayJumpCommand
extends SubCommand {
    public ReplayJumpCommand(AbstractCommand parent) {
        super(parent, "jump", "Jump to a specific moment", "jump <Time in Seconds>", true);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        Player p = (Player)cs;
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
            int duration = replayer.getReplay().getData().getDuration() / 20;
            if (MathUtils.isInt(args[1]) && Integer.valueOf(args[1]) > 0 && Integer.valueOf(args[1]) < duration) {
                int seconds = Integer.valueOf(args[1]);
                p.sendMessage("Jumping to §e" + seconds + " §7seconds...");
                ReplayAPI.getInstance().jumpToReplayTime(p, seconds);
            } else {
                p.sendMessage("§cTime has to be between 1 and " + (duration - 1));
            }
        } else {
            p.sendMessage("§cYou need to play a Replay first");
        }
        return true;
    }
}


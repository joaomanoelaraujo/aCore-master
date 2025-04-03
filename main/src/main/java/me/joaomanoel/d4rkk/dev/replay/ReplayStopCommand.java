/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.util.StringUtil
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public class ReplayStopCommand
extends SubCommand {
    public ReplayStopCommand(AbstractCommand parent) {
        super(parent, "stop", "Stops and saves a replay", "stop <Name> [Options]", false);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        boolean isNoSave;
        if (args.length > 3 || args.length < 2) {
            return false;
        }
        String name = args[1];
        boolean isForce = args.length == 3 && args[2].equals("-force");
        boolean bl = isNoSave = args.length == 3 && args[2].equals("-nosave");
        if (ReplayManager.activeReplays.containsKey(name) && ReplayManager.activeReplays.get(name).isRecording()) {
            Replay replay = ReplayManager.activeReplays.get(name);
            if (isNoSave || replay.getRecorder().getData().getActions().isEmpty()) {
                replay.getRecorder().stop(false);
                cs.sendMessage("§7Successfully stopped replay §e" + name);
            } else {
                if (ReplaySaver.exists(name) && !isForce) {
                    cs.sendMessage("§cReplay already exists. Use §o-force §r§cto overwrite or §o-nosave §r§cto discard the recording.");
                    return true;
                }
                cs.sendMessage("Saving replay §e" + name + "§7...");
                replay.getRecorder().stop(true);
                cs.sendMessage("§7Successfully saved replay");
            }
        } else {
            cs.sendMessage("§cReplay not found.");
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length == 3) {
            return Arrays.asList("-nosave", "-force");
        }
        return ReplayManager.activeReplays.keySet().stream().filter(name -> StringUtil.startsWithIgnoreCase(name, args.length > 1 ? args[1] : null)).collect(Collectors.toList());
    }
}


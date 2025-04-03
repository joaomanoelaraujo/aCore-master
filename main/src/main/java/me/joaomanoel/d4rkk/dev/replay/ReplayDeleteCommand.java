/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.util.StringUtil
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public class ReplayDeleteCommand
extends SubCommand {
    public ReplayDeleteCommand(AbstractCommand parent) {
        super(parent, "delete", "Deletes a replay", "delete <Name>", false);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        String name = args[1];
        if (ReplaySaver.exists(name)) {
            ReplaySaver.delete(name);
            cs.sendMessage("§aSuccessfully deleted replay.");
        } else {
            cs.sendMessage("§cReplay not found.");
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
        return ReplaySaver.getReplays().stream().filter(name -> StringUtil.startsWithIgnoreCase((String)name, args.length > 1 ? args[1] : null)).collect(Collectors.toList());
    }
}


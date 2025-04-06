/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.util.StringUtil
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class ReplayPlayCommand
extends SubCommand {
    public ReplayPlayCommand(AbstractCommand parent) {
        super(parent, "play", "Starts a recorded replay", "play <Name>", true);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        String name = args[1];
        final Player p = (Player)cs;
        if (ReplaySaver.exists(name) && !ReplayHelper.replaySessions.containsKey(p.getName())) {
            p.sendMessage("Loading replay §e" + name + "§7...");
            try {
                ReplaySaver.load(args[1], new Consumer<Replay>(){

                    @Override
                    public void accept(Replay replay) {
                        p.sendMessage("Replay loaded. Duration §e" + replay.getData().getDuration() / 20 + "§7 seconds.");
                        replay.play(p);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                p.sendMessage("§cError while loading §o" + name + ".replay. §r§cCheck console for more details.");
            }
        } else {
            p.sendMessage("§cReplay not found.");
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
        return ReplaySaver.getReplays().stream().filter(name -> StringUtil.startsWithIgnoreCase((String)name, args.length > 1 ? args[1] : null)).collect(Collectors.toList());
    }
}


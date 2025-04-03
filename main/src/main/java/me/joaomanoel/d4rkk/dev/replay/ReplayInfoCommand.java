/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.util.StringUtil
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.List;
import java.util.stream.Collectors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class ReplayInfoCommand
extends SubCommand {
    public ReplayInfoCommand(AbstractCommand parent) {
        super(parent, "info", "Information about a Replay", "info <Name>", false);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        String name = args[1];
        if (ReplaySaver.exists(name)) {
            cs.sendMessage("Loading replay §e" + name + "§7...");
            ReplaySaver.load(name, replay -> {
                ReplayInfo info = replay.getReplayInfo() != null ? replay.getReplayInfo() : new ReplayInfo(replay.getId(), replay.getData().getCreator(), null, replay.getData().getDuration());
                ReplayStats stats = ReplayOptimizer.analyzeReplay(replay);
                cs.sendMessage("§c\u00bb §7Information about §e§l" + replay.getId() + " §c\u00ab");
                cs.sendMessage("");
                if (info.getCreator() != null) {
                    cs.sendMessage("§7§oCreated by: §9" + info.getCreator());
                }
                cs.sendMessage("§7§oDuration: §6" + info.getDuration() / 20 + " §7§oseconds");
                cs.sendMessage("§7§oPlayers: §6" + stats.getPlayers().stream().collect(Collectors.joining("§7, §6")));
                cs.sendMessage("§7§oQuality: " + (replay.getData().getQuality() != null ? replay.getData().getQuality().getQualityName() : ReplayQuality.HIGH.getQualityName()));
                if (cs instanceof Player) {
                    ((Player)cs).spigot().sendMessage(this.buildMessage(stats));
                }
                if (stats.getEntityCount() > 0L) {
                    cs.sendMessage("§7§oEntities: §6" + stats.getEntityCount());
                }
            });
        } else {
            cs.sendMessage("§cReplay not found.");
        }
        return true;
    }

    public BaseComponent[] buildMessage(ReplayStats stats) {
        return new ComponentBuilder("§7§oRecorded data: ").append("§6§n" + stats.getActionCount() + "§r").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(stats.getSortedActions().entrySet().stream().map(e -> "§7" + e.getKey() + ": §b" + e.getValue()).collect(Collectors.joining("\n"))).create())).append(" §7§oactions").reset().create();
    }

    @Override
    public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
        return ReplaySaver.getReplays().stream().filter(name -> StringUtil.startsWithIgnoreCase((String)name, args.length > 1 ? args[1] : null)).collect(Collectors.toList());
    }
}


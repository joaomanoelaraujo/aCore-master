/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplayListCommand
extends SubCommand {
    public ReplayListCommand(AbstractCommand parent) {
        super(parent, "list", "Lists all replays", "list [Page]", false);
    }

    @Override
    public boolean execute(final CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length > 2) {
            return false;
        }
        if (ReplaySaver.getReplays().size() > 0) {
            int page = 1;
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            if (args.length == 2 && MathUtils.isInt(args[1])) {
                page = Integer.valueOf(args[1]);
            }
            List<String> replays = ReplaySaver.getReplays();
            replays.sort(this.dateComparator());
            CommandPagination<String> pagination = new CommandPagination<String>(replays, 9);
            cs.sendMessage("Available replays: §8(§6" + ReplaySaver.getReplays().size() + "§8) §7| Page: §e" + page + "§7/§e" + pagination.getPages());
            pagination.printPage(page, element -> {
                String message = String.format(" §6§o%s    §e%s", ReplayListCommand.this.getCreationDate(element) != null ? format.format(ReplayListCommand.this.getCreationDate(element)) : "", element);
                if (cs instanceof Player) {
                    BaseComponent[] comps;
                    if (DatabaseReplaySaver.getInfo(element) != null && Objects.requireNonNull(DatabaseReplaySaver.getInfo(element)).getCreator() != null) {
                        ReplayInfo info = DatabaseReplaySaver.getInfo(element);
                        comps = new ComponentBuilder(message).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Replay §e§l" + info.getID() + "\n\n§7Created by: §6" + info.getCreator() + "\n§7Duration: §6" + info.getDuration() / 20 + " §8sec\n\n§7Click to play!").create())).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/replay play " + info.getID())).create();
                    } else {
                        comps = new ComponentBuilder(message).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Replay §e§l" + element + "\n\n§7Click to play!").create())).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/replay play " + element)).create();
                    }
                    ((Player)cs).spigot().sendMessage(comps);
                } else {
                    cs.sendMessage(message);
                }
            });
        } else {
            cs.sendMessage("§cNo replays found.");
        }
        return true;
    }

    private Date getCreationDate(String replay) {
        if (ReplaySaver.replaySaver instanceof DefaultReplaySaver) {
            return new Date(new File(DefaultReplaySaver.DIR, replay + ".replay").lastModified());
        }
        if (ReplaySaver.replaySaver instanceof DatabaseReplaySaver) {
            return new Date(DatabaseReplaySaver.replayCache.get(replay).getTime());
        }
        return null;
    }

    private Comparator<String> dateComparator() {
        return (s1, s2) -> {
            if (this.getCreationDate(s1) != null && this.getCreationDate(s2) != null) {
                return Objects.requireNonNull(this.getCreationDate(s1)).compareTo(this.getCreationDate(s2));
            }
            return 0;
        };
    }

    @Override
    public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
        return IntStream.range(1, new CommandPagination<>(ReplaySaver.getReplays(), 9).getPages() + 1).boxed().map(String::valueOf).collect(Collectors.toList());
    }
}


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

public class ReplayMigrateCommand
extends SubCommand {
    private List<String> options = Arrays.asList("file", "database");

    public ReplayMigrateCommand(AbstractCommand parent) {
        super(parent, "migrate", "Migrate all replays", "migrate <File|Database>", false);
        this.setEnabled(false);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            return false;
        }
        String option = args[1].toLowerCase();
        if (this.options.contains(option)) {
            IReplaySaver migrationSaver = null;
            if (option.equalsIgnoreCase("file") && ReplaySaver.replaySaver instanceof DatabaseReplaySaver) {
                migrationSaver = new DefaultReplaySaver();
            } else if (option.equalsIgnoreCase("database") && ReplaySaver.replaySaver instanceof DefaultReplaySaver) {
                ConfigManager.USE_DATABASE = true;
                ConfigManager.loadData(false);
                migrationSaver = new DatabaseReplaySaver();
            } else {
                cs.sendMessage("§cYou can't migrate to the same system.");
                return true;
            }
            cs.sendMessage("§7Migrating replays to §e" + option);
            for (String replayName : ReplaySaver.getReplays()) {
                this.migrate(replayName, migrationSaver);
            }
        } else {
            cs.sendMessage("§cInvalid argument. " + this.options.stream().collect(Collectors.joining("|", "<", ">")));
        }
        return true;
    }

    private void migrate(final String replayName, final IReplaySaver saver) {
        ReplaySaver.load(replayName, new Consumer<Replay>(){

            @Override
            public void accept(Replay replay) {
                try {
                    if (!saver.replayExists(replayName)) {
                        LogUtils.log("Migrating " + replayName + "...");
                        saver.saveReplay(replay);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
        return this.options.stream().filter(option -> StringUtil.startsWithIgnoreCase((String)option, (String)args[1])).collect(Collectors.toList());
    }
}


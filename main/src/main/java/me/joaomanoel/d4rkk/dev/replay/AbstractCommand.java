/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.util.StringUtil
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public abstract class AbstractCommand
implements CommandExecutor,
TabCompleter {
    private String command;
    private String description;
    private String permission;
    private List<SubCommand> subCommands;
    private MessageFormat format;

    public AbstractCommand(String command, String description, String permission) {
        this.command = command;
        this.permission = permission;
        this.description = description;
        this.subCommands = new ArrayList<SubCommand>();
        this.format = this.setupFormat();
        this.subCommands = Arrays.asList(this.setupCommands());
    }

    public String getCommand() {
        return this.command;
    }

    public List<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    public String getPermission() {
        return this.permission;
    }

    protected abstract MessageFormat setupFormat();

    protected abstract SubCommand[] setupCommands();

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        String arg;
        String string = arg = args.length >= 1 ? args[0] : "overview";
        if (this.checkPermission(cs, arg)) {
            if (args.length == 0) {
                cs.sendMessage(this.description);
                for (SubCommand sub : this.subCommands) {
                    if (!this.checkPermission(cs, sub.getLabel().toLowerCase()) || !sub.isEnabled()) continue;
                    cs.sendMessage(this.format.getOverviewMessage(this.command, sub.getArgs(), sub.getDescription()));
                }
            } else {
                for (SubCommand sub : this.subCommands) {
                    if (!sub.getLabel().equalsIgnoreCase(arg) && !sub.getAliases().contains(arg)) continue;
                    if (sub.isPlayerOnly() && !(cs instanceof Player)) {
                        cs.sendMessage(this.format.getConsoleMessage());
                        return true;
                    }
                    if (!sub.execute(cs, cmd, label, args)) {
                        cs.sendMessage(this.format.getSyntaxMessage(this.command, sub.getArgs()));
                    }
                    return true;
                }
                cs.sendMessage(this.format.getNotFoundMessage());
            }
        } else {
            cs.sendMessage(this.format.getPermissionMessage());
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
        String perm;
        String string = perm = args.length >= 1 ? args[0] : "overview";
        if (!this.checkPermission(cs, perm)) {
            return new ArrayList<String>();
        }
        if (args.length == 1) {
            return this.subCommands.stream().map(SubCommand::getLabel).filter(name -> StringUtil.startsWithIgnoreCase((String)name, (String)args[0])).collect(Collectors.toList());
        }
        SubCommand sub = this.subCommands.stream().filter(sc -> sc.getLabel().equalsIgnoreCase(args[0]) || sc.getAliases().contains(args[0])).findAny().orElse(null);
        return sub != null ? sub.onTab(cs, cmd, label, args) : null;
    }

    private boolean checkPermission(CommandSender cs, String arg) {
        return this.permission == null || cs.hasPermission(this.permission + "." + arg);
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    private AbstractCommand parent;
    private String label;
    private String description;
    private String args;
    private boolean playerOnly;
    private boolean enabled = true;
    private List<String> aliases;

    public SubCommand(AbstractCommand parent, String label, String description, String args, boolean playerOnly) {
        this.parent = parent;
        this.label = label;
        this.description = description;
        this.args = args;
        this.playerOnly = playerOnly;
        this.aliases = new ArrayList<String>();
    }

    public abstract boolean execute(CommandSender var1, Command var2, String var3, String[] var4);

    public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
        return null;
    }

    public SubCommand addAlias(String alias) {
        if (!this.aliases.contains(alias)) {
            this.aliases.add(alias);
        }
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLabel() {
        return this.label;
    }

    public String getArgs() {
        return this.args;
    }

    public boolean isPlayerOnly() {
        return this.playerOnly;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public AbstractCommand getParent() {
        return this.parent;
    }
}


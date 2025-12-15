/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.replay.AbstractCommand;
import me.joaomanoel.d4rkk.dev.replay.MessageFormat;
import me.joaomanoel.d4rkk.dev.replay.ReplayDeleteCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayInfoCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayJumpCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayLeaveCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayListCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayMigrateCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayPlayCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayReformatCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayReloadCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayStartCommand;
import me.joaomanoel.d4rkk.dev.replay.ReplayStopCommand;
import me.joaomanoel.d4rkk.dev.replay.SubCommand;

public class ReplayCommand
extends AbstractCommand {
    public ReplayCommand() {
        super("Replay", "AdvancedReplay §ev" + Core.getInstance().getDescription().getVersion(), "replay.command");
    }

    @Override
    protected MessageFormat setupFormat() {
        return new MessageFormat().overview("§6/{command} {args} §7 - {desc}").syntax("Usage: §6/{command} {args}").permission("§cInsufficient permissions").notFound("§7Command not found.");
    }

    @Override
    protected SubCommand[] setupCommands() {
        return new SubCommand[]{new ReplayStartCommand(this), new ReplayStopCommand(this).addAlias("save"), new ReplayPlayCommand(this), new ReplayDeleteCommand(this).addAlias("remove"), new ReplayJumpCommand(this), new ReplayLeaveCommand(this), new ReplayInfoCommand(this), new ReplayListCommand(this), new ReplayReloadCommand(this), new ReplayReformatCommand(this), new ReplayMigrateCommand(this)};
    }
}


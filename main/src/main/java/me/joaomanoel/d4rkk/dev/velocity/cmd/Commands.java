package me.joaomanoel.d4rkk.dev.velocity.cmd;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import me.joaomanoel.d4rkk.dev.velocity.cmd.velocity.*;


public class Commands {

    public static void setupCommands(VelocityPlugin plugin) {
        CommandManager commandManager = plugin.getServer().getCommandManager();

        registerCommand(commandManager, new FakeCommand(plugin), "fake");
        registerCommand(commandManager, new FakeResetCommand(plugin), "fakereset");
        registerCommand(commandManager, new FakeListCommand(plugin), "fakelist");
        registerCommand(commandManager, new PartyCommand(plugin), "party");
        registerCommand(commandManager, new TellCommand(plugin), "tell", "w", "msg");
        registerCommand(commandManager, new StaffChatCommand(plugin), "sc", "staffchat");
        registerCommand(commandManager, new ReplyCommand(plugin), "r", "reply");
        registerCommand(commandManager, new PartyChatCommand(plugin), "p");
    }

    private static void registerCommand(CommandManager manager, SimpleCommand command, String name, String... aliases) {
        CommandMeta meta = manager.metaBuilder(name)
                .aliases(aliases)
                .build();
        manager.register(meta, command);
    }
}
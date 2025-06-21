package me.joaomanoel.d4rkk.dev.cmd;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.cmd.newcommands.*;
import me.joaomanoel.d4rkk.dev.player.fake.FakeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class Commands extends Command {
  
  public Commands(String name, String... aliases) {
    super(name);
    this.setAliases(Arrays.asList(aliases));
    
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      simpleCommandMap.register(this.getName(), "aCore", this);
    } catch (ReflectiveOperationException ex) {
      Core.getInstance().getLogger().log(Level.SEVERE, "Cannot register command: ", ex);
    }
  }
  
  public static void setupCommands() {
    new CoreCommand();
    new CoinsCommand();
    new ShopCommand();
    new ClearChatCommand();
    new CsmCommand();
    new BloodCommand();
    new FlyCommand();
    new TeleportCommand();
    new GameModeCommand();
    new TeleportHereCommand();
    new DiscordCommand();
    new InventorySeeCommand();
    new PingCommand();
    new VanishCommand();
    if (CashManager.CASH) {
      new CashCommand();
    }
    if (!FakeManager.isBungeeSide()) {
      new FakeCommand();
      new PartyCommand();
    }
  }
  
  public abstract void perform(CommandSender sender, String label, String[] args);
  
  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    this.perform(sender, commandLabel, args);
    return true;
  }
}

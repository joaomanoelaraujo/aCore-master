package me.joaomanoel.d4rkk.dev.cmd;

import me.joaomanoel.d4rkk.dev.cash.CashException;
import me.joaomanoel.d4rkk.dev.cash.CashManager;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand extends Commands {

  public CashCommand() {
    super("cash");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (args.length == 0) {
      if (sender instanceof Player) {
        sender.sendMessage("§eCash: §b" + StringUtils.formatNumber(CashManager.getCash(sender.getName())));
        return;
      }

      sender.sendMessage(
              " \n§6/cash set [player] [amount] §f- §7Set a player's cash.\n§6/cash add [player] [amount] §f- §7Give cash to a player.\n§6/cash remove [player] [amount] §f- §7Remove cash from a player.\n ");
      return;
    }

    if (!sender.hasPermission("aCore.cmd.cash")) {
      sender.sendMessage("§eCash: §b" + StringUtils.formatNumber(CashManager.getCash(sender.getName())));
      return;
    }

    String action = args[0];
    if (!action.equalsIgnoreCase("set") && !action.equalsIgnoreCase("add") && !action.equalsIgnoreCase("remove")) {
      sender.sendMessage(
              " \n§6/cash set [player] [amount] §f- §7Set a player's cash.\n§6/cash add [player] [amount] §f- §7Give cash to a player.\n§6/cash remove [player] [amount] §f- §7Remove cash from a player.\n ");
      return;
    }

    if (args.length <= 2) {
      sender.sendMessage("§cUsage: /cash " + action + " [player] [amount]");
      return;
    }

    long amount = 0L;
    try {
      if (args[2].startsWith("-")) {
        throw new NumberFormatException();
      }

      amount = Long.parseLong(args[2]);
    } catch (NumberFormatException ex) {
      sender.sendMessage("§cPlease use valid positive numbers.");
      return;
    }

    try {
      switch (action.toLowerCase()) {
        case "set":
          CashManager.setCash(args[1], amount);
          sender.sendMessage("§aYou set the cash of " + Role.getColored(args[1]) + " §ato §b" + StringUtils.formatNumber(amount) + "§a.");
          break;
        case "add":
          CashManager.addCash(args[1], amount);
          sender.sendMessage("§aYou added §b" + StringUtils.formatNumber(amount) + " §ato " + Role.getColored(args[1]) + "'s cash§a.");
          break;
        case "remove":
          CashManager.removeCash(args[1], amount);
          sender.sendMessage("§aYou removed §b" + StringUtils.formatNumber(amount) + " §afrom " + Role.getColored(args[1]) + "'s cash§a.");
      }
    } catch (CashException ex) {
      sender.sendMessage("§cThe user needs to be online.");
    }
  }
}

package me.joaomanoel.d4rkk.dev.cmd;

import me.joaomanoel.d4rkk.dev.menus.MenuProfile;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.command.CommandSender;

public class ACommand extends Commands{
    public ACommand() {
        super("a");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        new MenuProfile(Profile.getProfile(sender.getName()));
    }
}

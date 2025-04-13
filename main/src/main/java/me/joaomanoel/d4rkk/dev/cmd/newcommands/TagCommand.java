package me.joaomanoel.d4rkk.dev.cmd.newcommands;

import me.joaomanoel.d4rkk.dev.cmd.Commands;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.nms.NMSManager;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.utils.TagUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TagCommand extends Commands {
    private static final Map<String, Long> flood = new HashMap<>();
    private static final DecimalFormat df = new DecimalFormat("###.#");

    public TagCommand() {
        super("tag");
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
        } else {
            Player player = (Player)sender;
            Profile profile = Profile.getProfile(player.getName());
            List<Role> roles = this.getRole(player);
            if (profile != null) {
                if (!player.hasPermission("event.delay")) {
                    long start = flood.containsKey(player.getName()) ? (Long)flood.get(player.getName()) : 0L;
                    if (start > System.currentTimeMillis()) {
                        double time = (double)(start - System.currentTimeMillis()) / 1000.0D;
                        if (time > 0.1D) {
                            String timeString = df.format(time).replace(",", ".");
                            if (timeString.endsWith("0")) {
                                timeString = timeString.substring(0, timeString.lastIndexOf("."));
                            }

                            return;
                        }
                    }
                }

                if (args.length == 0) {
                    TextComponent component = new TextComponent("§aYour tags: \n");

                    if (roles.isEmpty()) {
                        Role memberTag = Role.getRoleByName("Member");
                        if (memberTag != null) {
                            roles.add(memberTag);
                        }
                    }

                    int max = roles.size();
                    int i = 0;

                    for (Role role : roles) {
                        TextComponent next = new TextComponent(role.getName().replace(" ", ""));
                        next.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tag " + role.getName().replace("§0", "").replace("§1", "").replace("§2", "").replace("§3", "").replace("§4", "").replace("§5", "").replace("§6", "").replace("§7", "").replace("§8", "").replace("§9", "").replace("§a", "").replace("§d", "").replace("§b", "").replace("§c", "").replace("§e", "").replace("§0§l", "").replace("§1§l", "").replace("§2§l", "").replace("§3§l", "").replace("§4§l", "").replace("§5§l", "").replace("§6§l", "").replace("§7§l", "").replace("§8§l", "").replace("§9§l", "").replace("§a§l", "").replace("§d§l", "").replace("§b§l", "").replace("§c§l", "").replace("§e§l", "")));
                        next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§fChange: " + role.getPrefix() + player.getName() + "\n§eClick to select!")));
                        component.addExtra(next);
                        ++i;
                        if (i < max) {
                            component.addExtra(new TextComponent("§f, "));
                        }
                    }

                    player.spigot().sendMessage(component);
                } else {
                    Role role = Role.getRoleByName(args[0]);
                    if (role != null && roles.contains(role)) {
                        NMSManager.sendActionBar("§fYour tag has been changed to: " + role.getName(), player);
                        flood.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10L));
                        DataContainer container = profile.getDataContainer("aCoreProfile", "tag");
                        container.set(role.getName().replace("§0", "").replace("§1", "").replace("§2", "").replace("§3", "").replace("§4", "").replace("§5", "").replace("§6", "").replace("§7", "").replace("§8", "").replace("§9", "").replace("§a", "").replace("§d", "").replace("§b", "").replace("§c", "").replace("§e", "").replace("§0§l", "").replace("§1§l", "").replace("§2§l", "").replace("§3§l", "").replace("§4§l", "").replace("§5§l", "").replace("§6§l", "").replace("§7§l", "").replace("§8§l", "").replace("§9§l", "").replace("§a§l", "").replace("§d§l", "").replace("§b§l", "").replace("§c§l", "").replace("§e§l", "").replace("+", ""));
                        TagUtils.setTag(player, role);



                    } else {
                        NMSManager.sendActionBar("§cInvalid tag for your current role.", player);
                    }
                }
            }

        }
    }

    private List<Role> getRole(Player player) {
        List<Role> list = new ArrayList<>();
        for (Role role : Role.listRoles()) {
            if (player.hasPermission(role.getPermission())) {
                list.add(role);
            }
        }

        return list;
    }
}

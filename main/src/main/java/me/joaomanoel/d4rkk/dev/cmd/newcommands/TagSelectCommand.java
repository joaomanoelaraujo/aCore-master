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

public class TagSelectCommand extends Commands {
    private static final Map<String, Long> flood = new HashMap<>();
    private static final DecimalFormat df = new DecimalFormat("###.#");

    public TagSelectCommand() {
        super("tag");
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());
        
        if (profile == null) {
            player.sendMessage("§cErro ao carregar seu perfil.");
            return;
        }

        if (!player.hasPermission("event.delay")) {
            long start = flood.getOrDefault(player.getName(), 0L);
            if (start > System.currentTimeMillis()) {
                double time = (double) (start - System.currentTimeMillis()) / 1000.0D;
                if (time > 0.1D) {
                    String timeString = df.format(time).replace(",", ".");
                    if (timeString.endsWith("0")) {
                        timeString = timeString.substring(0, timeString.lastIndexOf("."));
                    }
                    player.sendMessage("§cAguarde " + timeString + "s para usar este comando novamente.");
                    return;
                }
            }
        }
        if (args.length == 0) {
            List<Role> availableTags = getAvailableTags(player);
            
            if (availableTags.isEmpty()) {
                player.sendMessage("§cVocê não possui nenhuma tag disponível.");
                return;
            }

            TextComponent component = new TextComponent("§aSuas tags disponíveis:\n");
            
            int max = availableTags.size();
            int i = 0;

            for (Role role : availableTags) {
                String cleanName = stripColorCodes(role.getName());
                
                TextComponent next = new TextComponent(role.getName());
                next.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tag " + cleanName));
                next.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT, 
                    TextComponent.fromLegacyText("§fAlterar para: " + role.getPrefix() + player.getName() + "\n§eClique para selecionar!")
                ));
                
                component.addExtra(next);
                i++;
                
                if (i < max) {
                    component.addExtra(new TextComponent("§f, "));
                }
            }

            player.spigot().sendMessage(component);
            return;
        }

        String tagName = args[0];
        Role selectedRole = Role.getRoleByName(tagName);
        
        if (selectedRole == null) {
            player.sendMessage("§cTag não encontrada.");
            return;
        }

        List<Role> availableTags = getAvailableTags(player);
        
        if (!availableTags.contains(selectedRole)) {
            player.sendMessage("§cVocê não possui permissão para usar esta tag.");
            return;
        }

        DataContainer tagContainer = profile.getDataContainer("aCoreProfile", "tag");
        String cleanTagName = stripColorCodes(selectedRole.getName());
        tagContainer.set(cleanTagName);
        
        TagUtils.setTag(player);
        
        NMSManager.sendActionBar("§aSua tag foi alterada para: " + selectedRole.getName(), player);
        
        flood.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(3L));
    }

    private List<Role> getAvailableTags(Player player) {
        List<Role> availableTags = new ArrayList<>();
        
        for (Role role : Role.listRoles()) {
            if (player.hasPermission(role.getPermission()) || role.isDefault()) {
                availableTags.add(role);
            }
        }
        
        return availableTags;
    }

    private String stripColorCodes(String text) {
        if (text == null) return "";
        
        return text.replace("§0", "")
                   .replace("§1", "")
                   .replace("§2", "")
                   .replace("§3", "")
                   .replace("§4", "")
                   .replace("§5", "")
                   .replace("§6", "")
                   .replace("§7", "")
                   .replace("§8", "")
                   .replace("§9", "")
                   .replace("§a", "")
                   .replace("§b", "")
                   .replace("§c", "")
                   .replace("§d", "")
                   .replace("§e", "")
                   .replace("§f", "")
                   .replace("§l", "")
                   .replace("§m", "")
                   .replace("§n", "")
                   .replace("§o", "")
                   .replace("§r", "")
                   .replace("§k", "");
    }
}

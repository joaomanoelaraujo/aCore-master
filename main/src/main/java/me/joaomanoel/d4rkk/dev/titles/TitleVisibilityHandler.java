package me.joaomanoel.d4rkk.dev.titles;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class TitleVisibilityHandler {
    
    public static void handleJoinLobby(Profile profile, Map<String, TitleController> controllers) {
        if (profile.getName() == null) {
            return;
        }
        
        Player player = profile.getPlayer();
        if (player != null) {
            showExistingTitles(player, controllers);
        }
        
        TitleController controller = controllers.get(profile.getName());
        if (controller != null) {
            controller.enable();
        } else {
            Title title = profile.getSelectedContainer().getTitle();
            if (title != null && !controllers.containsKey(profile.getName())) {
                TitleSelectionHandler.selectTitle(profile, title, controllers);
            }
        }
    }
    
    public static void handleLeaveLobby(Profile profile, Map<String, TitleController> controllers) {
        TitleController controller = controllers.get(profile.getName());
        if (controller != null) {
            controller.disable();
        }
        
        Player player = profile.getPlayer();
        if (player != null) {
            hideExistingTitles(player, controllers);
        }
    }
    
    public static void showTitle(Profile profile, Profile target, TitleController controller) {
        if (controller == null) return;
        
        Player player = profile.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
            if (controller.getOwner() != null && player.isOnline() && player.canSee(controller.getOwner())) {
                controller.showToPlayer(player);
            }
        }, 10);
    }
    
    private static void showExistingTitles(Player player, Map<String, TitleController> controllers) {
        controllers.values().forEach(controller -> {
            if (controller.getOwner() != null && player.canSee(controller.getOwner())) {
                controller.showToPlayer(player);
            }
        });
    }
    
    private static void hideExistingTitles(Player player, Map<String, TitleController> controllers) {
        controllers.values().forEach(controller -> {
            if (controller.getOwner() != null && player.canSee(controller.getOwner())) {
                controller.hideToPlayer(player);
            }
        });
    }
}
package me.joaomanoel.d4rkk.dev.titles;

import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.BukkitUtils;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class TitleIconFactory {
    private static final String ICON_FORMAT = "%material%:%durability% : 1 : hide>all : name>%name% : desc>&fTitle: %title%\n \n%description%\n \n%action%";
    
    public static ItemStack createIcon(Title title, Profile profile) {
        boolean has = title.has(profile);
        Title selected = profile.getSelectedContainer().getTitle();
        boolean isSelected = selected != null && selected.equals(title);
        
        String material = has ? (isSelected ? "MAP" : "EMPTY_MAP") : "STAINED_GLASS_PANE";
        String durability = has ? "0" : "14";
        String nameColor = has ? "&a" : "&c";
        String action = has 
            ? (isSelected ? "&eClick to deselect!" : "&eClick to select!") 
            : "&cYou don't have this title.";
        
        return BukkitUtils.deserializeItemStack(
            ICON_FORMAT
                .replace("%material%", material)
                .replace("%durability%", durability)
                .replace("%name%", nameColor + StringUtils.stripColors(title.getTitle()))
                .replace("%title%", title.getTitle())
                .replace("%description%", title.getDescription())
                .replace("%action%", action)
        );
    }
}
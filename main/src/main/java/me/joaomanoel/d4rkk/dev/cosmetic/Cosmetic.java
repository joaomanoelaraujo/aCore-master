package me.joaomanoel.d4rkk.dev.cosmetic;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.cosmetic.container.CosmeticsContainer;
import me.joaomanoel.d4rkk.dev.cosmetic.container.SelectedContainer;
import me.joaomanoel.d4rkk.dev.cosmetic.types.ColoredTag;
import me.joaomanoel.d4rkk.dev.cosmetic.types.JoinMessage;
import me.joaomanoel.d4rkk.dev.cosmetic.types.MvpColor;
import me.joaomanoel.d4rkk.dev.cosmetic.types.PunchMessage;
import me.joaomanoel.d4rkk.dev.languages.GLanguage;
import me.joaomanoel.d4rkk.dev.player.Profile;
import me.joaomanoel.d4rkk.dev.utils.enums.EnumRarity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Cosmetic {

    private static final List<Cosmetic> COSMETICS = new ArrayList<>();
    protected long cash;
    protected EnumRarity rarity;
    private final long id;
    private final String permission;
    private final CosmeticType type;

    public Cosmetic(long id, CosmeticType type, String permission) {
        this.id = id;
        this.permission = permission;
        this.type = type;
        COSMETICS.add(this);
    }

    public static void setupCosmetics() {
        JoinMessage.setupJoinMessages();
        GLanguage.setupLanguage();
        PunchMessage.setupPunchMessages();
        MvpColor.setupColorMvp();
        ColoredTag.setupColoredTag();
    }
    public static void removeCosmetic(Cosmetic cosmetic) {
        COSMETICS.remove(cosmetic);
    }

    public static <T extends Cosmetic> T findById(Class<T> cosmeticClass, long id) {
        return COSMETICS.stream()
                .filter(cosmetic -> (cosmetic.getClass().isAssignableFrom(cosmeticClass) || cosmetic.getClass().getSuperclass().equals(cosmeticClass)) && cosmetic.getId() == id)
                .map(cosmetic -> (T) cosmetic).findFirst().orElse(null);
    }

    public static Cosmetic findById(String lootChestID) {
        return COSMETICS.stream().filter(cosmetic -> cosmetic.getLootChestsID().equals(lootChestID)).findFirst().orElse(null);
    }
    public static GLanguage findLanguageById(long languageId) {
        return COSMETICS.stream()
                .filter(cosmetic -> cosmetic instanceof GLanguage && cosmetic.getId() == languageId)
                .map(cosmetic -> (GLanguage) cosmetic)
                .findFirst()
                .orElse(null);
    }

    public static List<Cosmetic> listCosmetics() {
        return COSMETICS;
    }

    public static <T extends Cosmetic> List<T> listByType(Class<T> cosmeticClass) {
        return COSMETICS.stream().filter(cosmetic -> cosmetic.getClass().isAssignableFrom(cosmeticClass) || cosmetic.getClass().getSuperclass().equals(cosmeticClass))
                .sorted(Comparator.comparingLong(Cosmetic::getId)).map(cosmetic -> (T) cosmetic).collect(Collectors.toList());
    }

    protected static Object getAbsentProperty(String file, String property) {
        InputStream stream = Core.getInstance().getResource(file + ".yml");
        if (stream == null) {
            return null;
        }

        return YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8)).get(property);
    }

    public void give(Profile profile) {
        profile.getAbstractContainer("aCoreProfile", "cosmetics", CosmeticsContainer.class).addCosmetic(this);
    }

    public boolean has(Profile profile) {
        return profile.getAbstractContainer("aCoreProfile", "cosmetics", CosmeticsContainer.class).hasCosmetic(this);
    }

    public boolean isSelected(Profile profile) {
        return profile.getAbstractContainer("aCoreProfile", "cselected", SelectedContainer.class).isSelected(this);
    }



    public long getId() {
        return this.id;
    }

    public String getLootChestsID() {
        return "ac" + this.type.ordinal() + "-" + this.id;
    }

    public long getIndex() {
        return 1;
    }

    public EnumRarity getRarity() {
        return this.rarity;
    }

    public long getCash() {
        return this.cash;
    }

    public String getPermission() {
        return this.permission;
    }

    public CosmeticType getType() {
        return this.type;
    }

    public boolean canBuy(Player player) {
        return this.permission.isEmpty() || player.hasPermission(this.permission);
    }

    public abstract String getName();

    public abstract ItemStack getIcon(Profile profile);
}

package me.joaomanoel.d4rkk.dev.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;

import java.lang.reflect.Field;
import java.util.*;

public class BukkitUtils_1_20_R2 implements BukkitUtilsItf {

    @Override
    public void openBook(Player player, ItemStack book) {
        ItemStack old = player.getInventory().getItemInHand();

        player.getInventory().setItemInHand(book);
        player.openBook(book);

        player.getInventory().setItemInHand(old);
        player.updateInventory();
    }

    private Material resolveMaterial(String name) {
        String upper = name.toUpperCase();

        // 1) trata ID:SUBID (incluindo "159" sem subid) para terracota colorida
        String[] parts = upper.split(":", 2);
        String base = parts[0];
        String sub  = parts.length > 1 ? parts[1] : null;

        if ("159".equals(base)) {  // antigo ID de terracota
            if (sub != null) {
                return switch (sub) {
                    case "13" -> Material.GREEN_TERRACOTTA;
                    case "14" -> Material.RED_TERRACOTTA;
                    // ...outras cores, se precisar
                    default  -> Material.TERRACOTTA;
                };
            } else {
                return Material.TERRACOTTA;
            }
        }

        // 2) se não era terracota, joga fora o subid e continua
        upper = base;

        switch (upper) {
            case "373":
                return Material.POTION;
            case "SKULL_ITEM":
                return Material.PLAYER_HEAD;
            case "369":
                return Material.BLAZE_ROD;
            case "383":
                return Material.ENDER_DRAGON_SPAWN_EGG;
            case "351":
            case "INK_SACK":
                return Material.GRAY_DYE;
            case "WOOD":
                return Material.OAK_PLANKS;
            case "FIREWORK":
                return Material.FIREWORK_ROCKET;
            case "WOOL":
                return Material.WHITE_WOOL;
            case "BOOK_AND_QUILL":
                return Material.WRITABLE_BOOK;
            case "160":
            case "STAINED_GLASS_PANE":
                return Material.WHITE_STAINED_GLASS_PANE;
            case "299":
                return Material.LEATHER_CHESTPLATE;
            case "145":
                return Material.ANVIL;
            case "395":
                return Material.WRITTEN_BOOK;
            case "421":
                return Material.NAME_TAG;
            case "416":
                return Material.LEAD;
            case "268":
                return Material.WOODEN_SWORD;
            case "322":
                return Material.GOLDEN_APPLE;
            case "32":
                return Material.DEAD_BUSH;
            case "294":
                return Material.GOLDEN_PICKAXE;
            case "379":
                return Material.BREWING_STAND;
            case "86":
                return Material.PUMPKIN;
            case "332":
                return Material.SNOWBALL;
            case "38":
                return Material.POPPY;
            case "BED":
                return Material.RED_BED;
            case "259":
                return Material.FLINT_AND_STEEL;
            case "347":
                return Material.CLOCK;
            case "REDSTONE_COMPARATOR":
                return Material.COMPARATOR;
            default:
                try {
                    return Material.valueOf(upper);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning("[aCore] Material desconhecido ao desserializar: " + name);
                    return Material.BARRIER;
                }
        }
    }


    @Override
    public ItemStack deserializeItemStack(String item) {
        if (item == null || item.isEmpty()) {
            return new ItemStack(Material.AIR);
        }

        item = StringUtils.formatColors(item).replace("\\n", "\n");
        String[] split = item.split(" : ");
        String mat = split[0].split(":")[0];

        Material material = resolveMaterial(mat);
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();

        BookMeta book = meta instanceof BookMeta ? ((BookMeta) meta) : null;
        SkullMeta skull = meta instanceof SkullMeta ? ((SkullMeta) meta) : null;
        PotionMeta potion = meta instanceof PotionMeta ? ((PotionMeta) meta) : null;
        FireworkEffectMeta effect = meta instanceof FireworkEffectMeta ? ((FireworkEffectMeta) meta) : null;
        LeatherArmorMeta armor = meta instanceof LeatherArmorMeta ? ((LeatherArmorMeta) meta) : null;
        EnchantmentStorageMeta enchantment = meta instanceof EnchantmentStorageMeta ? ((EnchantmentStorageMeta) meta) : null;

        if (split.length > 1) {
            stack.setAmount(Math.min(Integer.parseInt(split[1]), 64));
        }

        List<String> lore = new ArrayList<>();
        for (int i = 2; i < split.length; i++) {
            String opt = split[i];

            if (opt.startsWith("name>")) {
                meta.setDisplayName(StringUtils.formatColors(opt.split(">")[1]));
            } else if (opt.startsWith("desc>")) {
                for (String lored : opt.split(">")[1].split("\n")) {
                    lore.add(StringUtils.formatColors(lored));
                }
            } else if (opt.startsWith("enchant>")) {
                for (String enchanted : opt.split(">")[1].split("\n")) {
                    if (enchantment != null) {
                        enchantment.addStoredEnchant(Enchantment.getByName(enchanted.split(":")[0]), Integer.parseInt(enchanted.split(":")[1]), true);
                        continue;
                    }
                    meta.addEnchant(Enchantment.getByName(enchanted.split(":")[0]), Integer.parseInt(enchanted.split(":")[1]), true);
                }
            } else if (opt.startsWith("paint>") && (effect != null || armor != null)) {
                for (String color : opt.split(">")[1].split("\n")) {
                    String[] rgb = color.split(":");
                    if (rgb.length == 3) {
                        Color c = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                        if (armor != null) {
                            armor.setColor(c);
                        } else if (effect != null) {
                            effect.setEffect(FireworkEffect.builder().withColor(c).build());
                        }
                    }
                }
            } else if (opt.startsWith("owner>") && skull != null) {
                String playerName = opt.split(">")[1];
                skull.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
            } else if (opt.startsWith("skin>") && skull != null) {
                String texture = opt.length() > 5 ? opt.substring(5) : null;

                if (texture != null && !texture.isEmpty()) {
                    GameProfile gp = new GameProfile(UUID.randomUUID(), "Head");
                    gp.getProperties().put("textures", new Property("textures", texture));
                    try {
                        Field profileField = meta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        profileField.set(meta, gp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (opt.startsWith("pages>") && book != null) {
                book.setPages(opt.split(">")[1].split("\\{pular}"));
            } else if (opt.startsWith("author>") && book != null) {
                book.setAuthor(opt.split(">")[1]);
            } else if (opt.startsWith("title>") && book != null) {
                book.setTitle(opt.split(">")[1]);
            } else if (opt.startsWith("effect>") && potion != null) {
                for (String pe : opt.split(">")[1].split("\n")) {
                    String[] effectSplit = pe.split(":");
                    PotionEffectType type = PotionEffectType.getByName(effectSplit[0]);
                    int amplifier = Integer.parseInt(effectSplit[1]);
                    int duration = Integer.parseInt(effectSplit[2]);
                    potion.addCustomEffect(new PotionEffect(type, duration, amplifier), false);
                }
            } else if (opt.startsWith("hide>")) {
                String[] flags = opt.split(">")[1].split("\n");
                for (String flag : flags) {
                    if (flag.equalsIgnoreCase("all")) {
                        meta.addItemFlags(ItemFlag.values());
                        break;
                    } else {
                        try {
                            meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            }
        }

        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }

        stack.setItemMeta(meta);
        return stack;
    }

    public static Sound getSoundSafe(String name) {
        try {
            return Sound.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[aCore] Som desconhecido: " + name + " — usando CLICK como fallback");
            return Sound.UI_BUTTON_CLICK;
        }
    }

    @Override
    public String serializeItemStack(ItemStack item) {
        StringBuilder sb = new StringBuilder(item.getType().name() + (item.getDurability() != 0 ? ":" + item.getDurability() : "") + " : " + item.getAmount());
        ItemMeta meta = item.getItemMeta();

        BookMeta book = meta instanceof BookMeta ? ((BookMeta) meta) : null;
        SkullMeta skull = meta instanceof SkullMeta ? ((SkullMeta) meta) : null;
        PotionMeta potion = meta instanceof PotionMeta ? ((PotionMeta) meta) : null;
        FireworkEffectMeta effect = meta instanceof FireworkEffectMeta ? ((FireworkEffectMeta) meta) : null;
        LeatherArmorMeta armor = meta instanceof LeatherArmorMeta ? ((LeatherArmorMeta) meta) : null;
        EnchantmentStorageMeta enchantment = meta instanceof EnchantmentStorageMeta ? ((EnchantmentStorageMeta) meta) : null;

        if (meta.hasDisplayName()) {
            sb.append(" : name>").append(StringUtils.deformatColors(meta.getDisplayName()));
        }

        if (meta.hasLore()) {
            sb.append(" : desc>");
            for (int i = 0; i < meta.getLore().size(); i++) {
                String line = meta.getLore().get(i);
                sb.append(line).append(i + 1 == meta.getLore().size() ? "" : "\n");
            }
        }

        if (meta.hasEnchants() || (enchantment != null && enchantment.hasStoredEnchants())) {
            sb.append(" : enchant>");
            int size = 0;
            for (Map.Entry<Enchantment, Integer> entry : (enchantment != null ? enchantment.getStoredEnchants() : meta.getEnchants()).entrySet()) {
                int level = entry.getValue();
                String name = entry.getKey().getName();
                sb.append(name).append(":").append(level).append(++size == (enchantment != null ? enchantment.getStoredEnchants() : meta.getEnchants()).size() ? "" : "\n");
            }
        }

        if (skull != null && !skull.getOwner().isEmpty()) {
            sb.append(" : owner>").append(skull.getOwner());
        }

        if (book != null && book.hasPages()) {
            sb.append(" : pages>").append(StringUtils.join(book.getPages(), "{pular}"));
        }

        if (book != null && book.hasTitle()) {
            sb.append(" : title>").append(book.getTitle());
        }

        if (book != null && book.hasAuthor()) {
            sb.append(" : author>").append(book.getAuthor());
        }

        if ((effect != null && effect.hasEffect() && !effect.getEffect().getColors().isEmpty()) || (armor != null && armor.getColor() != null)) {
            Color color = effect != null ? effect.getEffect().getColors().get(0) : armor.getColor();
            sb.append(" : paint>").append(color.getRed()).append(":").append(color.getGreen()).append(":").append(color.getBlue());
        }

        if (potion != null && potion.hasCustomEffects()) {
            sb.append(" : effect>");
            int size = 0;
            for (PotionEffect pe : potion.getCustomEffects()) {
                sb.append(pe.getType().getName()).append(":").append(pe.getAmplifier()).append(":").append(pe.getDuration()).append(++size == potion.getCustomEffects().size() ? "" : "\n");
            }
        }

        for (ItemFlag flag : meta.getItemFlags()) {
            sb.append(" : hide>").append(flag.name());
        }

        return StringUtils.deformatColors(sb.toString()).replace("\n", "\\n");
    }

    @Override
    public ItemStack putProfileOnSkull(Player player, ItemStack head) {
        if (head == null || !(head.getItemMeta() instanceof SkullMeta)) {
            return head;
        }

        ItemMeta meta = head.getItemMeta();
        ((SkullMeta) meta).setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;
    }

    @Override
    public ItemStack putProfileOnSkull(Object profile, ItemStack head) {
        if (head == null || !(head.getItemMeta() instanceof SkullMeta)) {
            return head;
        }

        ItemMeta meta = head.getItemMeta();
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        head.setItemMeta(meta);
        return head;
    }

    @Override
    public void putGlowEnchantment(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

}

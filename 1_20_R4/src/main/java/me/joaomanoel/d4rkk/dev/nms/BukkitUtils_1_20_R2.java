package me.joaomanoel.d4rkk.dev.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.joaomanoel.d4rkk.dev.nms.particle.ParticleOptions;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.Particles;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.ParticleUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;

import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

public class BukkitUtils_1_20_R2 implements BukkitUtilsItf {
    private static final Map<String, Material> LEGACY_IDS = new HashMap<>();
    private static final Map<Material, String> MATERIAL_TO_ID = new HashMap<>();
    private static final Map<String, Material> LEGACY_TO_NEW = new HashMap<>();
    static {
        LEGACY_IDS.put("383:99", Material.IRON_GOLEM_SPAWN_EGG);
        LEGACY_IDS.put("386", Material.WRITABLE_BOOK);
        LEGACY_IDS.put("160:0", Material.GRAY_STAINED_GLASS_PANE);
        LEGACY_IDS.put("35:0", Material.WHITE_WOOL);
        LEGACY_IDS.put("35:1", Material.ORANGE_WOOL);
        LEGACY_IDS.put("35:2", Material.MAGENTA_WOOL);
        LEGACY_IDS.put("LIT_PUMPKIN", Material.JACK_O_LANTERN);
        LEGACY_IDS.put("WEB", Material.COBWEB);
        LEGACY_IDS.put("91", Material.JACK_O_LANTERN);
        LEGACY_IDS.put("35:3", Material.LIGHT_BLUE_WOOL);
        LEGACY_IDS.put("35:4", Material.YELLOW_WOOL);
        LEGACY_IDS.put("35:5", Material.LIME_WOOL);
        LEGACY_IDS.put("35:6", Material.PINK_WOOL);
        LEGACY_IDS.put("35:7", Material.GRAY_WOOL);
        LEGACY_IDS.put("35:8", Material.LIGHT_GRAY_WOOL);
        LEGACY_IDS.put("35:9", Material.CYAN_WOOL);
        LEGACY_IDS.put("35:10", Material.PURPLE_WOOL);
        LEGACY_IDS.put("35:11", Material.BLUE_WOOL);
        LEGACY_IDS.put("35:12", Material.BROWN_WOOL);
        LEGACY_IDS.put("339", Material.PAPER);
        LEGACY_IDS.put("WOOD:1", Material.SPRUCE_PLANKS);
        LEGACY_IDS.put("WOOD:2", Material.BIRCH_PLANKS);
        LEGACY_IDS.put("WOOD:3", Material.JUNGLE_PLANKS);
        LEGACY_IDS.put("WOOD:4", Material.ACACIA_PLANKS);
        LEGACY_IDS.put("WOOD:5", Material.DARK_OAK_PLANKS);
        LEGACY_IDS.put("5:1", Material.SPRUCE_PLANKS);
        LEGACY_IDS.put("5:2", Material.BIRCH_PLANKS);
        LEGACY_IDS.put("5:3", Material.JUNGLE_PLANKS);
        LEGACY_IDS.put("5:4", Material.ACACIA_PLANKS);
        LEGACY_IDS.put("5:5", Material.DARK_OAK_PLANKS);
        LEGACY_IDS.put("WOOL:0", Material.WHITE_WOOL);
        LEGACY_IDS.put("WOOL:1", Material.ORANGE_WOOL);
        LEGACY_IDS.put("WOOL:2", Material.MAGENTA_WOOL);
        LEGACY_IDS.put("WOOL:3", Material.LIGHT_BLUE_WOOL);
        LEGACY_IDS.put("WOOL:4", Material.YELLOW_WOOL);
        LEGACY_IDS.put("WOOL:5", Material.LIME_WOOL);
        LEGACY_IDS.put("WOOL:6", Material.PINK_WOOL);
        LEGACY_IDS.put("WOOL:7", Material.GRAY_WOOL);
        LEGACY_IDS.put("WOOL:8", Material.LIGHT_GRAY_WOOL);
        LEGACY_IDS.put("WOOL:9", Material.CYAN_WOOL);
        LEGACY_IDS.put("WOOL:10", Material.PURPLE_WOOL);
        LEGACY_IDS.put("WOOL:11", Material.BLUE_WOOL);
        LEGACY_IDS.put("WOOL:12", Material.BROWN_WOOL);

        LEGACY_IDS.put("351:1", Material.RED_DYE);
        LEGACY_IDS.put("351:8", Material.GRAY_DYE);
        LEGACY_IDS.put("351:11", Material.YELLOW_DYE);
        LEGACY_IDS.put("351:10", Material.LIME_DYE);
        LEGACY_IDS.put("270", Material.WOODEN_PICKAXE);
        LEGACY_IDS.put("271", Material.WOODEN_AXE);
        LEGACY_IDS.put("285", Material.GOLDEN_PICKAXE);
        LEGACY_IDS.put("286", Material.GOLDEN_AXE);
        LEGACY_IDS.put("257", Material.IRON_PICKAXE);
        LEGACY_IDS.put("258", Material.IRON_AXE);
        LEGACY_IDS.put("278", Material.DIAMOND_PICKAXE);
        LEGACY_IDS.put("279", Material.DIAMOND_AXE);
        LEGACY_IDS.put("385", Material.FIRE_CHARGE);
        LEGACY_IDS.put("121", Material.END_STONE);
        LEGACY_IDS.put("76", Material.REDSTONE_TORCH);
        LEGACY_IDS.put("159", Material.WHITE_TERRACOTTA);
        LEGACY_IDS.put("373", Material.POTION);
        LEGACY_IDS.put("SKULL_ITEM", Material.PLAYER_HEAD);
        LEGACY_IDS.put("369", Material.BLAZE_ROD);
        LEGACY_IDS.put("383", Material.ENDER_DRAGON_SPAWN_EGG);
        LEGACY_IDS.put("351", Material.GRAY_DYE);
        LEGACY_IDS.put("INK_SACK", Material.GRAY_DYE);
        LEGACY_IDS.put("WOOD", Material.OAK_PLANKS);
        LEGACY_IDS.put("FIREWORK", Material.FIREWORK_ROCKET);
        LEGACY_IDS.put("WOOL", Material.WHITE_WOOL);
        LEGACY_IDS.put("BOOK_AND_QUILL", Material.WRITABLE_BOOK);
        LEGACY_IDS.put("160", Material.WHITE_STAINED_GLASS_PANE);
        LEGACY_IDS.put("STAINED_GLASS_PANE", Material.WHITE_STAINED_GLASS_PANE);
        LEGACY_IDS.put("299", Material.LEATHER_CHESTPLATE);
        LEGACY_IDS.put("145", Material.ANVIL);
        LEGACY_IDS.put("395", Material.FILLED_MAP);
        LEGACY_IDS.put("421", Material.NAME_TAG);
        LEGACY_IDS.put("416", Material.LEAD);
        LEGACY_IDS.put("268", Material.WOODEN_SWORD);
        LEGACY_IDS.put("322", Material.GOLDEN_APPLE);
        LEGACY_IDS.put("32", Material.DEAD_BUSH);
        LEGACY_IDS.put("294", Material.GOLDEN_PICKAXE);
        LEGACY_IDS.put("379", Material.BREWING_STAND);
        LEGACY_IDS.put("86", Material.PUMPKIN);
        LEGACY_IDS.put("332", Material.SNOWBALL);
        LEGACY_IDS.put("38", Material.POPPY);
        LEGACY_IDS.put("BED", Material.RED_BED);
        LEGACY_IDS.put("259", Material.FLINT_AND_STEEL);
        LEGACY_IDS.put("323", Material.OAK_SIGN);
        LEGACY_IDS.put("347", Material.CLOCK);
    }
    static {
        LEGACY_TO_NEW.put("383:99", Material.IRON_GOLEM_SPAWN_EGG);
        LEGACY_TO_NEW.put("383:50", Material.CREEPER_SPAWN_EGG);
        LEGACY_TO_NEW.put("383:51", Material.SKELETON_SPAWN_EGG);
        LEGACY_TO_NEW.put("383:52", Material.SPIDER_SPAWN_EGG);
        LEGACY_TO_NEW.put("383:54", Material.ZOMBIE_SPAWN_EGG);
        LEGACY_TO_NEW.put("383:55", Material.SLIME_SPAWN_EGG);

        LEGACY_TO_NEW.put("35:14", Material.RED_WOOL);
        LEGACY_TO_NEW.put("35:6", Material.PINK_WOOL);
        LEGACY_TO_NEW.put("35:9", Material.CYAN_WOOL);
        LEGACY_TO_NEW.put("35:11", Material.BLUE_WOOL);
        LEGACY_TO_NEW.put("35:0", Material.WHITE_WOOL);
        LEGACY_TO_NEW.put("35:1", Material.ORANGE_WOOL);
        LEGACY_TO_NEW.put("35:10", Material.PURPLE_WOOL);
        LEGACY_TO_NEW.put("35:13", Material.LIME_WOOL);
    }
    static {
        register("383:99", Material.IRON_GOLEM_SPAWN_EGG);
        register("270", Material.WOODEN_PICKAXE);
        register("271", Material.WOODEN_AXE);
        register("285", Material.GOLDEN_PICKAXE);
        register("286", Material.GOLDEN_AXE);
        register("257", Material.IRON_PICKAXE);
        register("258", Material.IRON_AXE);
        register("278", Material.DIAMOND_PICKAXE);
        register("279", Material.DIAMOND_AXE);
        register("385", Material.FIRE_CHARGE); // era LEGACY_FIREBALL
        register("121", Material.END_STONE);
        register("76", Material.REDSTONE_TORCH);
        register("159", Material.WHITE_TERRACOTTA);
        register("373", Material.POTION);
        register("SKULL_ITEM", Material.PLAYER_HEAD);
        register("369", Material.BLAZE_ROD);
        register("383", Material.ENDER_DRAGON_SPAWN_EGG);
        register("351", Material.GRAY_DYE);
        register("INK_SACK", Material.GRAY_DYE);
        register("WOOD", Material.OAK_PLANKS);
        register("FIREWORK", Material.FIREWORK_ROCKET);
        register("WOOL", Material.WHITE_WOOL);
        register("BOOK_AND_QUILL", Material.WRITABLE_BOOK);
        register("160", Material.WHITE_STAINED_GLASS_PANE);
        register("STAINED_GLASS_PANE", Material.WHITE_STAINED_GLASS_PANE);
        register("299", Material.LEATHER_CHESTPLATE);
        register("145", Material.ANVIL);
        register("395", Material.FILLED_MAP);
        register("421", Material.NAME_TAG);
        register("416", Material.LEAD);
        register("268", Material.WOODEN_SWORD);
        register("322", Material.GOLDEN_APPLE);
        register("32", Material.DEAD_BUSH);
        register("294", Material.GOLDEN_PICKAXE);
        register("379", Material.BREWING_STAND);
        register("86", Material.PUMPKIN);
        register("332", Material.SNOWBALL);
        register("38", Material.POPPY);
        register("BED", Material.RED_BED);
        register("259", Material.FLINT_AND_STEEL);
        register("323", Material.OAK_SIGN);
        register("347", Material.CLOCK);
        register("REDSTONE_COMPARATOR", Material.COMPARATOR);
        register("383:99", Material.IRON_GOLEM_SPAWN_EGG);
        register("383:50", Material.CREEPER_SPAWN_EGG);
        register("383:51", Material.SKELETON_SPAWN_EGG);
        register("383:54", Material.ZOMBIE_SPAWN_EGG);
        register("383:55", Material.SLIME_SPAWN_EGG);
        register("383:57", Material.SPIDER_SPAWN_EGG);
        register("383:58", Material.CAVE_SPIDER_SPAWN_EGG);
        register("383:61", Material.BLAZE_SPAWN_EGG);
        register("383:62", Material.MAGMA_CUBE_SPAWN_EGG);
        register("383:65", Material.BAT_SPAWN_EGG);
        register("383:66", Material.WITCH_SPAWN_EGG);
        register("383:90", Material.PIG_SPAWN_EGG);
        register("383:91", Material.SHEEP_SPAWN_EGG);
        register("383:92", Material.COW_SPAWN_EGG);
        register("383:93", Material.CHICKEN_SPAWN_EGG);
        register("383:95", Material.WOLF_SPAWN_EGG);
        register("383:96", Material.MOOSHROOM_SPAWN_EGG);
        register("383:98", Material.OCELOT_SPAWN_EGG);
    }

    public static ItemStack translate(String legacyId, int amount) {
        Material mat = LEGACY_TO_NEW.get(legacyId);
        if (mat != null) {
            return new ItemStack(mat, amount);
        }

        // fallback → tenta direto
        String[] split = legacyId.split(":");
        Material fallback = Material.matchMaterial(split[0]);
        if (fallback == null) {
            fallback = Material.STONE;
        }
        return new ItemStack(fallback, amount);
    }

    private static void register(String id, Material mat) {
        LEGACY_IDS.put(id.toUpperCase(), mat);
        MATERIAL_TO_ID.put(mat, id.toUpperCase());
    }

    private static void register(Material mat, String id) {
        register(id, mat); // chama o primeiro corretamente
    }


    public static Material getMaterial(String legacyId) {
        return LEGACY_IDS.get(legacyId);
    }

    public static String getLegacyId(Material mat) {
        return MATERIAL_TO_ID.get(mat);
    }
    @Override
    public void openBook(Player player, ItemStack book) {
        ItemStack old = player.getInventory().getItemInHand();

        player.getInventory().setItemInHand(book);
        player.openBook(book);

        player.getInventory().setItemInHand(old);
        player.updateInventory();
    }

    private Material resolveMaterial(String name, short data) {
        if (name == null || name.isEmpty()) {
            return Material.BARRIER;
        }

        String upper = name.toUpperCase();

        // 1) tenta pelo mapa legado completo (tipo "35:4", "383:99")
        String legacyKey = upper + (data > 0 ? ":" + data : "");
        if (LEGACY_IDS.containsKey(legacyKey)) {
            return LEGACY_IDS.get(legacyKey);
        }

        // 2) tenta pelo nome simples no mapa legado
        if (LEGACY_IDS.containsKey(upper)) {
            return LEGACY_IDS.get(upper);
        }

        // 3) tenta pelo enum moderno
        try {
            return Material.valueOf(upper);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[aCore] Material desconhecido: " + name + (data > 0 ? ":" + data : ""));
            return Material.BARRIER;
        }
    }





    @Override
    public ItemStack deserializeItemStack(String raw) {
        if (raw == null || raw.isEmpty()) {
            return new ItemStack(Material.BARRIER);
        }

        raw = StringUtils.formatColors(raw).replace("\\n", "\n");

        // token completo da esquerda (pode ser "383:99", "WOOL:14", "IRON_GOLEM_SPAWN_EGG", etc.)
        String[] parts = raw.split("\\s* : \\s*", 3);
        if (parts.length < 2) {
            Bukkit.getLogger().warning("[aCore] Entrada inválida: " + raw);
            return new ItemStack(Material.BARRIER);
        }

        String leftToken = parts[0].trim().toUpperCase();

        // ✅ 1) tenta RESOLVER PRIMEIRO PELO MAPA LEGADO usando o token COMPLETO

        String[] matSplit = leftToken.split(":", 2);
        String materialToken = matSplit[0].trim();
        short data = 0;
        if (matSplit.length > 1) {
            try {
                data = Short.parseShort(matSplit[1].trim());
            } catch (NumberFormatException ignore) {}
        }

        Material mat = resolveMaterial(materialToken, data);

        // quantidade
        int amount = 1;
        try {
            amount = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException ignore) { }

        ItemStack item;

        // poções antigas ainda suportadas pela sua sintaxe
        if (mat == Material.POTION || mat == Material.SPLASH_POTION || mat == Material.LINGERING_POTION) {
            boolean splash = (mat == Material.SPLASH_POTION) || (mat == Material.POTION && (data & 0x4000) != 0);
            Material potionMat = splash ? Material.SPLASH_POTION : Material.POTION;
            item = new ItemStack(potionMat, amount);

            PotionMeta pm = (PotionMeta) item.getItemMeta();
            boolean upgraded = (data & 0x20) != 0;
            boolean extended = (data & 0x2000) != 0;
            int baseId = data & 0xF;

            PotionType baseType = switch (baseId) {
                case 1 -> PotionType.REGENERATION;
                case 2 -> PotionType.SWIFTNESS;
                case 9 -> PotionType.STRENGTH;
                case 11 -> PotionType.AWKWARD;
                default -> PotionType.NIGHT_VISION;
            };

            PotionType finalType = baseType;
            if (upgraded) {
                finalType = switch (baseType) {
                    case REGENERATION -> PotionType.STRONG_REGENERATION;
                    case SWIFTNESS -> PotionType.STRONG_SWIFTNESS;
                    case STRENGTH -> PotionType.STRONG_STRENGTH;
                    case AWKWARD -> PotionType.AWKWARD;
                    default -> baseType;
                };
            } else if (extended) {
                finalType = switch (baseType) {
                    case REGENERATION -> PotionType.LONG_REGENERATION;
                    case SWIFTNESS -> PotionType.LONG_SWIFTNESS;
                    case STRENGTH -> PotionType.LONG_STRENGTH;
                    case AWKWARD -> PotionType.AWKWARD;
                    default -> baseType;
                };
            }

            pm.setBasePotionType(finalType);
            item.setItemMeta(pm);

        } else {
            // mantém compatibilidade com dados quando ainda for relevante pro seu remapper
            if (data != 0) {
                item = new ItemStack(mat, amount, data);
            } else {
                item = new ItemStack(mat, amount);
            }
        }

        if (parts.length == 3) {
            String[] props = parts[2].split("\\s+");
            raw = StringUtils.formatColors(raw).replace("\\n", "\n");
            String[] split = raw.split(" : ");
            ItemMeta meta = item.getItemMeta();

            BookMeta book = meta instanceof BookMeta ? (BookMeta) meta : null;
            SkullMeta skull = meta instanceof SkullMeta ? (SkullMeta) meta : null;
            PotionMeta potion = meta instanceof PotionMeta ? (PotionMeta) meta : null;
            FireworkEffectMeta effect = meta instanceof FireworkEffectMeta ? (FireworkEffectMeta) meta : null;
            LeatherArmorMeta armor = meta instanceof LeatherArmorMeta ? (LeatherArmorMeta) meta : null;
            EnchantmentStorageMeta enchantment = meta instanceof EnchantmentStorageMeta ? (EnchantmentStorageMeta) meta : null;

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
//                } else if (opt.startsWith("base>") && potion != null) {
//                String[] bd = opt.split(">")[1].split(":");
//                PotionType  type     = PotionType.valueOf(bd[0].toUpperCase());
//                boolean     extended = Boolean.parseBoolean(bd[1]);
//                boolean     upgraded = Boolean.parseBoolean(bd[2]);
//                potion.setBasePotionData(new PotionData(type, extended, upgraded));
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
                        int amplifier = Integer.parseInt(effectSplit[1]) - 1;
                        int duration = Integer.parseInt(effectSplit[2]);
                        potion.addCustomEffect(new PotionEffect(type, duration, amplifier), false);
                    }
                } else if (opt.startsWith("hide>")) {
                    String[] flags = opt.split(">")[1].split("\n");
                    if (flags[0].equalsIgnoreCase("all")) {
                        meta.addItemFlags(ItemFlag.values());
                        hideAttributes(meta);
                        break;
                    }

                    for (String flag : flags) {
                        meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                    }
                }
            }
            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public void hideAttributes(ItemMeta meta) {
        if (meta == null) return;
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "dummy", 0, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    public static Sound getSoundSafe(String name) {
        try {
            return Sound.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[aCore] Som desconhecido: " + name + " — usando CLICK como fallback");
            return Sound.UI_BUTTON_CLICK;
        }
    }

    private BufferedImage glyphImage;

    @Override
    public void showIn(Player origin, Location location) {
        if (glyphImage == null) return;

        // distancia vertical acima do jogador
        Location base = location.clone().add(0, 1.5, 0);

        int w = glyphImage.getWidth();
        int h = glyphImage.getHeight();
        double scale = 0.1;

        for (int ix = 0; ix < w; ix++) {
            for (int iy = 0; iy < h; iy++) {
                int argb = glyphImage.getRGB(ix, iy);
                int alpha = (argb >>> 24) & 0xFF;
                if (alpha < 128) continue;  // pixel transparente

                // posição relativa à base
                double fx = (ix - w / 2.0) * scale;
                double fy = (iy - h / 2.0) * scale;
                Location particleLoc = base.clone().add(fx, fy, 0);

                // extrai componentes RGB (0–255)
                int ri = (argb >> 16) & 0xFF;
                int gi = (argb >> 8)  & 0xFF;
                int bi =  argb        & 0xFF;

                // cria as opções de pó colorido
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(ri, gi, bi), (float) scale);

                // dispara somente para o jogador
                origin.spawnParticle(
                        Particle.DUST,    // tipo
                        particleLoc,          // local
                        1,                    // count
                        0, 0, 0,              // offsets (não usados em DustOptions)
                        dust                  // dados: cor e tamanho
                );
            }
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
    public ItemStack applyNTBTag(ItemStack item, List<Object> lines) {
        try {
            List<Object> pages = new ArrayList<>();
            BookMeta meta = (BookMeta) item.getItemMeta();
            Field field = meta.getClass().getDeclaredField("pages");
            field.setAccessible(true);
            IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a((String) lines.get(0), getRegistryProvider());
            pages.add(page);
            field.set(meta, pages);

            item.setItemMeta(meta);
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void putGlowEnchantment(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    @Override
    public void displayParticle(Player viewer, String particleName, boolean isFar, float x, float y, float z, float offSetX, float offSetY, float offSetZ, float speed, int count) {
        EntityPlayer cp = ((CraftPlayer) viewer).getHandle();
        try {
            Class<?> particlesClass = Class.forName("net.minecraft.core.particles.ParticleTypes");
            Class<?> particleParamClass = Class.forName("net.minecraft.core.particles.ParticleParam");
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket");
            Field particleField = particlesClass.getField(particleName.toUpperCase());
            Object particle = particleField.get(null);

            Constructor<?> constructor = packetClass.getConstructor(
                    particleParamClass, boolean.class,
                    double.class, double.class, double.class,
                    float.class, float.class, float.class,
                    float.class, int.class
            );

            Object packet = constructor.newInstance(
                    particle, isFar,
                    x, y, z,
                    offSetX, offSetY, offSetZ,
                    speed, count
            );

            cp.transferCookieConnection.sendPacket((net.minecraft.network.protocol.Packet<?>) packet);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void displayParticle(Player viewer, ParticleOptions options, boolean isFar, float x, float y, float z, float offSetX, float offSetY, float offSetZ, float speed, int count) {
        EntityPlayer cp = ((CraftPlayer) viewer).getHandle();
        try {
            Class<?> particleParamClass = Class.forName("net.minecraft.core.particles.ParticleParam");
            Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket");
            Constructor<?> constructor = packetClass.getConstructor(
                    particleParamClass, boolean.class,
                    double.class, double.class, double.class,
                    float.class, float.class, float.class,
                    float.class, int.class
            );

            Object packet = constructor.newInstance(
                    options.makeOption(), isFar,
                    x, y, z,
                    offSetX, offSetY, offSetZ,
                    speed, count
            );

            cp.transferCookieConnection.sendPacket((net.minecraft.network.protocol.Packet<?>) packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGlow(Player player, boolean glow) {
        player.setGlowing(glow);
    }


    public static HolderLookup.a getRegistryProvider() {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        try {
            Method registryAccessMethod = MinecraftServer.class.getDeclaredMethod("registryAccess");
            registryAccessMethod.setAccessible(true);

            Object registryAccess = registryAccessMethod.invoke(server);
            if (registryAccess instanceof HolderLookup.a provider) {
                return provider;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

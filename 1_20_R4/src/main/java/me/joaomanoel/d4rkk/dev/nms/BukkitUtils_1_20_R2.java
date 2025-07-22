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

    @Override
    public void openBook(Player player, ItemStack book) {
        ItemStack old = player.getInventory().getItemInHand();

        player.getInventory().setItemInHand(book);
        player.openBook(book);

        player.getInventory().setItemInHand(old);
        player.updateInventory();
    }

    private Material resolveMaterial(String name) {
        if (name == null || name.isEmpty()) {
            return Material.BARRIER;
        }

        // 1) separa nome/base do data legada
        String upper = name.toUpperCase();
        String[] parts = upper.split(":", 2);
        String base = parts[0].trim();
        String subid = parts.length > 1 ? parts[1].trim() : null;

        // 2) terracota colorida (ID 159)
        if ("159".equals(base)) {
            if (subid != null) {
                return switch (subid) {
                    case "13" -> Material.GREEN_TERRACOTTA;
                    case "14" -> Material.RED_TERRACOTTA;
                    default -> Material.TERRACOTTA;
                };
            }
            return Material.TERRACOTTA;
        }

        // 3) poções (ID 373 + data)
        if ("373".equals(base)) {
            if (subid != null) {
                int d = Integer.parseInt(subid);
                boolean splash = (d & 0x4000) != 0;  // flag 16384
                // já mapeamos o material, o meta virá depois
                return splash ? Material.SPLASH_POTION : Material.POTION;
            }
            return Material.POTION;
        }
        // 2) se não era terracota, joga fora o subid e continua
        upper = base;

        switch (upper) {
            case "270":
                return Material.WOODEN_PICKAXE;
            case "271":
                return Material.WOODEN_AXE;
            case "285":
                return Material.GOLDEN_PICKAXE;
            case "286":
                return Material.GOLDEN_AXE;
            case "257":
                return Material.IRON_PICKAXE;
            case "258":
                return Material.IRON_AXE;
            case "278":
                return Material.DIAMOND_PICKAXE;
            case "279":
                return Material.DIAMOND_AXE;
            case "385":
                return Material.LEGACY_FIREBALL;
            case "121":
                return Material.END_STONE;
            case "76":
                return Material.REDSTONE_TORCH;
            case "159":
                return Material.WHITE_TERRACOTTA;
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
                return Material.FILLED_MAP;
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
            case "323":
                return Material.OAK_SIGN;
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
    public ItemStack deserializeItemStack(String raw) {
        if (raw == null || raw.isEmpty()) {
            return new ItemStack(Material.BARRIER);
        }

        raw = StringUtils.formatColors(raw).replace("\\n", "\n");

        String[] parts = raw.split("\\s* : \\s*", 3);
        if (parts.length < 2) {
            Bukkit.getLogger().warning("[aCore] Entrada inválida: " + raw);
            return new ItemStack(Material.BARRIER);
        }

        String[] matSplit = parts[0].split(":", 2);
        String materialToken = matSplit[0].trim();
        short data = 0;
        if (matSplit.length > 1) {
            try {
                data = Short.parseShort(matSplit[1].trim());
            } catch (NumberFormatException ignore) {
            }
        }
        Material mat = resolveMaterial(materialToken);

        int amount = 1;
        try {
            amount = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException ignore) {
        }

        ItemStack item;
        if (mat == Material.POTION
                || mat == Material.SPLASH_POTION
                || mat == Material.LINGERING_POTION) {

            boolean splash = (mat == Material.SPLASH_POTION)
                    || (mat == Material.POTION && (data & 0x4000) != 0);
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

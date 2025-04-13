package me.joaomanoel.d4rkk.dev.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.joaomanoel.d4rkk.dev.utils.StringUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
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

    @Override
    public ItemStack deserializeItemStack(String item) {
        if (item == null || item.isEmpty()) {
            return new ItemStack(Material.AIR);
        }

        item = StringUtils.formatColors(item).replace("\\n", "\n");
        String[] split = item.split(" : ");
        String mat = split[0].split(":")[0];

        Material material = Material.valueOf(mat);
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

            if (opt.startsWith("nome>")) {
                meta.setDisplayName(StringUtils.formatColors(opt.split(">")[1]));
            } else if (opt.startsWith("desc>")) {
                for (String lored : opt.split(">")[1].split("\n")) {
                    lore.add(StringUtils.formatColors(lored));
                }
            } else if (opt.startsWith("encantar>")) {
                for (String enchanted : opt.split(">")[1].split("\n")) {
                    if (enchantment != null) {
                        enchantment.addStoredEnchant(Enchantment.getByName(enchanted.split(":")[0]), Integer.parseInt(enchanted.split(":")[1]), true);
                        continue;
                    }

                    meta.addEnchant(Enchantment.getByName(enchanted.split(":")[0]), Integer.parseInt(enchanted.split(":")[1]), true);
                }
            } else if (opt.startsWith("pintar>") && (effect != null || armor != null)) {
                for (String color : opt.split(">")[1].split("\n")) {
                    if (color.split(":").length > 2) {
                        if (armor != null) {
                            armor.setColor(Color.fromRGB(Integer.parseInt(color.split(":")[0]), Integer.parseInt(color.split(":")[1]), Integer.parseInt(color.split(":")[2])));
                        } else if (effect != null) {
                            effect.setEffect(FireworkEffect.builder()
                                    .withColor(Color.fromRGB(Integer.parseInt(color.split(":")[0]), Integer.parseInt(color.split(":")[1]), Integer.parseInt(color.split(":")[2]))).build());
                        }
                    }
                }
            } else if (opt.startsWith("dono>") && skull != null) {
                skull.setOwner(opt.split(">")[1]);
            } else if (opt.startsWith("skin>") && skull != null) {
                GameProfile gp = new GameProfile(UUID.randomUUID(), "CUSTOM_HEAD");
                gp.getProperties().put("textures", new Property("textures", opt.split(">")[1]));
                try {
                    Field profileField = meta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(meta, gp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (opt.startsWith("paginas>") && book != null) {
                book.setPages(opt.split(">")[1].split("\\{pular}"));
            } else if (opt.startsWith("autor>") && book != null) {
                book.setAuthor(opt.split(">")[1]);
            } else if (opt.startsWith("titulo>") && book != null) {
                book.setTitle(opt.split(">")[1]);
            } else if (opt.startsWith("efeito>") && potion != null) {
                for (String pe : opt.split(">")[1].split("\n")) {
                    potion.addCustomEffect(new PotionEffect(PotionEffectType.getByName(pe.split(":")[0]), Integer.parseInt(pe.split(":")[2]), Integer.parseInt(pe.split(":")[1])), false);
                }
            } else if (opt.startsWith("esconder>")) {
                String[] flags = opt.split(">")[1].split("\n");
                for (String flag : flags) {
                    if (flag.equalsIgnoreCase("tudo")) {
                        meta.addItemFlags(ItemFlag.values());
                        break;
                    } else {
                        meta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
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
            sb.append(" : nome>").append(StringUtils.deformatColors(meta.getDisplayName()));
        }

        if (meta.hasLore()) {
            sb.append(" : desc>");
            for (int i = 0; i < meta.getLore().size(); i++) {
                String line = meta.getLore().get(i);
                sb.append(line).append(i + 1 == meta.getLore().size() ? "" : "\n");
            }
        }

        if (meta.hasEnchants() || (enchantment != null && enchantment.hasStoredEnchants())) {
            sb.append(" : encantar>");
            int size = 0;
            for (Map.Entry<Enchantment, Integer> entry : (enchantment != null ? enchantment.getStoredEnchants() : meta.getEnchants()).entrySet()) {
                int level = entry.getValue();
                String name = entry.getKey().getName();
                sb.append(name).append(":").append(level).append(++size == (enchantment != null ? enchantment.getStoredEnchants() : meta.getEnchants()).size() ? "" : "\n");
            }
        }

        if (skull != null && !skull.getOwner().isEmpty()) {
            sb.append(" : dono>").append(skull.getOwner());
        }

        if (book != null && book.hasPages()) {
            sb.append(" : paginas>").append(StringUtils.join(book.getPages(), "{pular}"));
        }

        if (book != null && book.hasTitle()) {
            sb.append(" : titulo>").append(book.getTitle());
        }

        if (book != null && book.hasAuthor()) {
            sb.append(" : autor>").append(book.getAuthor());
        }

        if ((effect != null && effect.hasEffect() && !effect.getEffect().getColors().isEmpty()) || (armor != null && armor.getColor() != null)) {
            Color color = effect != null ? effect.getEffect().getColors().get(0) : armor.getColor();
            sb.append(" : pintar>").append(color.getRed()).append(":").append(color.getGreen()).append(":").append(color.getBlue());
        }

        if (potion != null && potion.hasCustomEffects()) {
            sb.append(" : efeito>");
            int size = 0;
            for (PotionEffect pe : potion.getCustomEffects()) {
                sb.append(pe.getType().getName()).append(":").append(pe.getAmplifier()).append(":").append(pe.getDuration()).append(++size == potion.getCustomEffects().size() ? "" : "\n");
            }
        }

        for (ItemFlag flag : meta.getItemFlags()) {
            sb.append(" : esconder>").append(flag.name());
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

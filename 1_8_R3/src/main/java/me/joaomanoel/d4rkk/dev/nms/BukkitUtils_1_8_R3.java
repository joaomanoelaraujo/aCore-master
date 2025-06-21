package me.joaomanoel.d4rkk.dev.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.joaomanoel.d4rkk.dev.reflection.Accessors;
import me.joaomanoel.d4rkk.dev.reflection.MinecraftReflection;
import me.joaomanoel.d4rkk.dev.reflection.acessors.ConstructorAccessor;
import me.joaomanoel.d4rkk.dev.reflection.acessors.FieldAccessor;
import me.joaomanoel.d4rkk.dev.reflection.acessors.MethodAccessor;
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

import java.lang.reflect.Field;
import java.util.*;

public class BukkitUtils_1_8_R3 implements BukkitUtilsItf {

    public static final List<FieldAccessor<Color>> COLORS;

    public static final MethodAccessor GET_PROFILE;
    public static final FieldAccessor<GameProfile> SKULL_META_PROFILE;

    private static final Map<Class<?>, MethodAccessor> getHandleCache = new HashMap<>();

    private static final Class<?> NBTagList = MinecraftReflection.getMinecraftClass("NBTTagList");
    private static final Class<?> NBTagString = MinecraftReflection.getMinecraftClass("NBTTagString");

    private static final ConstructorAccessor<?> constructorTagList = new ConstructorAccessor<>(NBTagList.getConstructors()[0]);
    private static final ConstructorAccessor<?> constructorTagString = new ConstructorAccessor<>(NBTagString.getConstructors()[1]);

    private static final MethodAccessor getTag = Accessors.getMethod(MinecraftReflection.getItemStackClass(), "getTag");
    private static final MethodAccessor setCompound = Accessors.getMethod(MinecraftReflection.getNBTTagCompoundClass(), "set", String.class, NBTagList.getSuperclass());
    private static final MethodAccessor addList = Accessors.getMethod(NBTagList, "add");
    private static final MethodAccessor asNMSCopy = Accessors.getMethod(MinecraftReflection.getCraftItemStackClass(), "asNMSCopy");
    private static final MethodAccessor asCraftMirror = Accessors.getMethod(MinecraftReflection.getCraftItemStackClass(), "asCraftMirror");

    static {
        COLORS = new ArrayList<>();
        for (Field field : Color.class.getDeclaredFields()) {
            if (field.getType().equals(Color.class)) {
                COLORS.add(new FieldAccessor<>(field));
            }
        }
        GET_PROFILE = Accessors.getMethod(MinecraftReflection.getCraftBukkitClass("entity.CraftPlayer"), GameProfile.class, 0);
        SKULL_META_PROFILE = Accessors.getField(MinecraftReflection.getCraftBukkitClass("inventory.CraftMetaSkull"), "profile", GameProfile.class);
    }

    public static Object getHandle(Object target) {
        try {
            Class<?> clazz = target.getClass();
            MethodAccessor accessor = getHandleCache.get(clazz);
            if (accessor == null) {
                accessor = Accessors.getMethod(clazz, "getHandle");
                getHandleCache.put(clazz, accessor);
            }

            return accessor.invoke(target);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot find method getHandle() for " + target + ".");
        }
    }


    @Override
    public void openBook(Player player, ItemStack book) {
        Object entityPlayer = getHandle(player);

        ItemStack old = player.getInventory().getItemInHand();
        try {
            player.getInventory().setItemInHand(book);
            Accessors.getMethod(entityPlayer.getClass(), "openBook").invoke(entityPlayer, asNMSCopy(book));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

        ItemStack stack = new ItemStack(Material.matchMaterial(mat), 1);
        if (split[0].split(":").length > 1) {
            stack.setDurability((short) Integer.parseInt(split[0].split(":")[1]));
        }

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
                    if (color.split(":").length > 2) {
                        if (armor != null) {
                            armor.setColor(Color.fromRGB(Integer.parseInt(color.split(":")[0]), Integer.parseInt(color.split(":")[1]), Integer.parseInt(color.split(":")[2])));
                        } else if (effect != null) {
                            effect.setEffect(FireworkEffect.builder()
                                    .withColor(Color.fromRGB(Integer.parseInt(color.split(":")[0]), Integer.parseInt(color.split(":")[1]), Integer.parseInt(color.split(":")[2]))).build());
                        }
                        continue;
                    }

                    for (FieldAccessor<Color> field : COLORS) {
                        if (field.getHandle().getName().equals(color.toUpperCase())) {
                            if (armor != null) {
                                armor.setColor(field.get(null));
                            } else if (effect != null) {
                                effect.setEffect(FireworkEffect.builder().withColor(field.get(null)).build());
                            }
                            break;
                        }
                    }
                }
            } else if (opt.startsWith("owner>") && skull != null) {
                skull.setOwner(opt.split(">")[1]);
            } else if (opt.startsWith("skin>") && skull != null) {
                GameProfile gp = new GameProfile(UUID.randomUUID(), null);
                gp.getProperties().put("textures", new Property("textures", opt.split(">")[1]));
                SKULL_META_PROFILE.set(skull, gp);
            } else if (opt.startsWith("pages>") && book != null) {
                book.setPages(opt.split(">")[1].split("\\{pular}"));
            } else if (opt.startsWith("author>") && book != null) {
                book.setAuthor(opt.split(">")[1]);
            } else if (opt.startsWith("title>") && book != null) {
                book.setTitle(opt.split(">")[1]);
            } else if (opt.startsWith("effect>") && potion != null) {
                for (String pe : opt.split(">")[1].split("\n")) {
                    potion.addCustomEffect(new PotionEffect(PotionEffectType.getByName(pe.split(":")[0]), Integer.parseInt(pe.split(":")[2]), Integer.parseInt(pe.split(":")[1])), false);
                }
            } else if (opt.startsWith("hide>")) {
                String[] flags = opt.split(">")[1].split("\n");
                for (String flag : flags) {
                    if (flag.equalsIgnoreCase("all")) {
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
        SKULL_META_PROFILE.set(meta, (GameProfile) GET_PROFILE.invoke(player));
        head.setItemMeta(meta);
        return head;
    }

    @Override
    public ItemStack putProfileOnSkull(Object profile, ItemStack head) {
        if (head == null || !(head.getItemMeta() instanceof SkullMeta)) {
            return head;
        }

        ItemMeta meta = head.getItemMeta();
        SKULL_META_PROFILE.set(meta, (GameProfile) profile);
        head.setItemMeta(meta);
        return head;
    }

    @Override
    public ItemStack applyNTBTag(ItemStack item, List<Object> lines) {
        Object nmsStack = asNMSCopy(item);
        Object compound = getTag.invoke(nmsStack);
        Object compoundList = constructorTagList.newInstance();
        for (Object string : lines) {
            addList.invoke(compoundList, constructorTagString.newInstance(string));
        }

        setCompound.invoke(compound, "pages", compoundList);
        return asCraftMirror(nmsStack);
    }

    @Override
    public void putGlowEnchantment(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    public static Object asNMSCopy(ItemStack item) {
        return asNMSCopy.invoke(null, item);
    }

    /**
     * Transforma um ItemStack do NMS em {@link ItemStack}
     *
     * @param nmsItem Item NMS para transformar.
     * @return ItemStack
     */
    public static ItemStack asCraftMirror(Object nmsItem) {
        return (ItemStack) asCraftMirror.invoke(null, nmsItem);
    }

    public static ItemStack setNBTList(ItemStack item, String key, List<String> strings) {
        Object nmsStack = asNMSCopy(item);
        Object compound = getTag.invoke(nmsStack);
        Object compoundList = constructorTagList.newInstance();
        for (String string : strings) {
            addList.invoke(compoundList, constructorTagString.newInstance(string));
        }
        setCompound.invoke(compound, key, compoundList);
        return asCraftMirror(nmsStack);
    }
}

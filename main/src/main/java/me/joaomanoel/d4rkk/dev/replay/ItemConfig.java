package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ItemConfig {

    public static File file = new File(Core.getInstance().getDataFolder(), "items.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    public static HashMap<ItemConfigType, ItemConfigOption> items = new HashMap<>();

    public static void loadConfig() {
        addDefaults();

        // Cria arquivo padrão se não existir
        if (!file.exists()) {
            for (ItemConfigType type : items.keySet()) {
                String name = type.name().toLowerCase();
                ItemConfigOption item = items.get(type);

                cfg.set("items." + name + ".name", item.getName());
                cfg.set("items." + name + ".id",
                        item.getMaterial().name() + (item.getData() != 0 ? ":" + item.getData() : ""));
                cfg.set("items." + name + ".slot", item.getSlot());
                if (item.getOwner() != null) {
                    cfg.set("items." + name + ".owner", item.getOwner());
                }
                cfg.set("items." + name + ".enabled", item.isEnabled());
            }

            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadData() {
        for (ItemConfigType type : ItemConfigType.values()) {
            String name = type.name().toLowerCase();
            String displayName = cfg.getString("items." + name + ".name", "&7Item");
            String owner = cfg.getString("items." + name + ".owner");
            String matString = cfg.getString("items." + name + ".id", "STONE").toUpperCase();
            int slot = cfg.getInt("items." + name + ".slot", 0);
            boolean enabled = cfg.getBoolean("items." + name + ".enabled", true);

            int data = 0;
            Material material;

            try {
                // Permite formato "ID" ou "ID:DATA" ou "MATERIAL[:DATA]"
                if (matString.contains(":")) {
                    String[] split = matString.split(":");
                    String first = split[0];
                    String second = split[1];

                    // Tenta converter o primeiro número para ID
                    int id = tryParseInt(first);
                    if (id != -1) {
                        material = MaterialBridge.fromID(id);
                    } else {
                        material = MaterialBridge.fromName(first);
                    }

                    data = tryParseInt(second);
                    if (data == -1) data = 0;
                } else {
                    // Só ID ou nome sem data
                    int id = tryParseInt(matString);
                    if (id != -1) {
                        material = MaterialBridge.fromID(id);
                    } else {
                        material = MaterialBridge.fromName(matString);
                    }
                }

                if (material == null) material = Material.AIR;
            } catch (Exception e) {
                material = Material.AIR;
            }

            items.put(type, new ItemConfigOption(material, displayName, slot, owner, data).enable(enabled));
        }
    }

    public static ItemConfigOption getItem(ItemConfigType type) {
        return items.get(type);
    }

    public static ItemConfigType getByIdAndName(Material material, String name) {
        for (ItemConfigType type : items.keySet()) {
            ItemConfigOption option = items.get(type);
            if (option == null || option.getMaterial() == null) continue;

            String optionMat = option.getMaterial().name();
            String compareMat = material.name();

            if (optionMat.equalsIgnoreCase(compareMat) && option.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    private static void addDefaults() {
        Material compass = Material.COMPASS;
        Material watch = MaterialBridge.WATCH.toMaterial();
        Material door = MaterialBridge.WOOD_DOOR.toMaterial();
        Material skull = MaterialBridge.SKULL_ITEM.toMaterial();
        Material slime = MaterialBridge.SLIME_BLOCK.toMaterial();

        items.put(ItemConfigType.TELEPORT,
                new ItemConfigOption(compass, "&7Teleport", 0));
        items.put(ItemConfigType.SPEED,
                new ItemConfigOption(watch, "&cSlow &8[&eRight&8] &aFast &8[&eShift Right&8]", 1));
        items.put(ItemConfigType.LEAVE,
                new ItemConfigOption(door, "&7Leave replay", 8));
        items.put(ItemConfigType.FORWARD,
                new ItemConfigOption(skull, "&a» &e10 seconds", 5, "MHF_ArrowRight", 3));
        items.put(ItemConfigType.BACKWARD,
                new ItemConfigOption(skull, "&c« &e10 seconds", 3, "MHF_ArrowLeft", 3));
        items.put(ItemConfigType.RESUME,
                new ItemConfigOption(slime, "&aResume", 4));
        items.put(ItemConfigType.PAUSE,
                new ItemConfigOption(skull, "&cPause", 4, "Push_red_button", 3));
    }

    private static int tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class EntityMappings {
    private static EntityMappings instance;
    private BiMap<String, Integer> entityLookup = HashBiMap.create();

    private EntityMappings() {
        this.createMappings();
    }

    public String getType(int id) {
        return (String)this.entityLookup.inverse().get(id);
    }

    public int getTypeId(String entityType) {
        return this.entityLookup.getOrDefault(entityType, 0);
    }

    private void createMappings() {
        this.entityLookup.put("AXOLOTL", 3);
        this.entityLookup.put("BAT", 4);
        this.entityLookup.put("BEE", 5);
        this.entityLookup.put("BLAZE", 6);
        this.entityLookup.put("CAT", 8);
        this.entityLookup.put("CAVE_SPIDER", 9);
        this.entityLookup.put("CHICKEN", 10);
        this.entityLookup.put("COD", 11);
        this.entityLookup.put("COW", 12);
        this.entityLookup.put("CREEPER", 13);
        this.entityLookup.put("DOLPHIN", 14);
        this.entityLookup.put("DONKEY", 15);
        this.entityLookup.put("DROWNED", 17);
        this.entityLookup.put("ELDER_GUARDIAN", 18);
        this.entityLookup.put("ENDER_DRAGON", 20);
        this.entityLookup.put("ENDERMAN", 21);
        this.entityLookup.put("ENDERMITE", 22);
        this.entityLookup.put("EVOKER", 23);
        this.entityLookup.put("FOX", 29);
        this.entityLookup.put("GHAST", 30);
        this.entityLookup.put("GIANT", 31);
        this.entityLookup.put("GLOW_SQUID", 33);
        this.entityLookup.put("GOAT", 34);
        this.entityLookup.put("GUARDIAN", 35);
        this.entityLookup.put("HOGLIN", 36);
        this.entityLookup.put("HORSE", 37);
        this.entityLookup.put("HUSK", 38);
        this.entityLookup.put("ILLUSIONER", 39);
        this.entityLookup.put("IRON_GOLEM", 40);
        this.entityLookup.put("LLAMA", 46);
        this.entityLookup.put("MAGMA_CUBE", 48);
        this.entityLookup.put("MULE", 57);
        this.entityLookup.put("MUSHROOM_COW", 58);
        this.entityLookup.put("OCELOT", 59);
        this.entityLookup.put("PANDA", 61);
        this.entityLookup.put("PARROT", 62);
        this.entityLookup.put("PHANTOM", 63);
        this.entityLookup.put("PIG", 64);
        this.entityLookup.put("PIGLIN", 65);
        this.entityLookup.put("PIGLIN_BRUTE", 66);
        this.entityLookup.put("PILLAGER", 67);
        this.entityLookup.put("POLAR_BEAR", 68);
        this.entityLookup.put("PUFFERFISH", 70);
        this.entityLookup.put("RABBIT", 71);
        this.entityLookup.put("RAVAGER", 72);
        this.entityLookup.put("SALMON", 73);
        this.entityLookup.put("SHEEP", 74);
        this.entityLookup.put("SHULKER", 75);
        this.entityLookup.put("SILVERFISH", 77);
        this.entityLookup.put("SKELETON", 78);
        this.entityLookup.put("SKELETON_HORSE", 79);
        this.entityLookup.put("SLIME", 80);
        this.entityLookup.put("SNOWMAN", 82);
        this.entityLookup.put("SPIDER", 85);
        this.entityLookup.put("SQUID", 86);
        this.entityLookup.put("STRAY", 87);
        this.entityLookup.put("STRIDER", 88);
        this.entityLookup.put("TRADER_LLAMA", 94);
        this.entityLookup.put("TROPICAL_FISH", 95);
        this.entityLookup.put("TURTLE", 96);
        this.entityLookup.put("VEX", 97);
        this.entityLookup.put("VILLAGER", 98);
        this.entityLookup.put("VINDICATOR", 99);
        this.entityLookup.put("WANDERING_TRADER", 100);
        this.entityLookup.put("WITCH", 101);
        this.entityLookup.put("WITHER", 102);
        this.entityLookup.put("WITHER_SKELETON", 103);
        this.entityLookup.put("WOLF", 105);
        this.entityLookup.put("ZOGLIN", 106);
        this.entityLookup.put("ZOMBIE", 107);
        this.entityLookup.put("ZOMBIE_HORSE", 108);
        this.entityLookup.put("ZOMBIE_VILLAGER", 109);
        this.entityLookup.put("ZOMBIFIED_PIGLIN", 110);
        this.entityLookup.put("WARDEN", 111);
        this.entityLookup.put("FROG", 112);
        this.entityLookup.put("TADPOLE", 113);
        this.entityLookup.put("ALLAY", 114);
    }

    public static EntityMappings getInstance() {
        if (instance == null) {
            instance = new EntityMappings();
        }
        return instance;
    }
}


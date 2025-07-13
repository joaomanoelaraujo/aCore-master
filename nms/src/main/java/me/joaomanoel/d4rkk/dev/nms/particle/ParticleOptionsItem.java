package me.joaomanoel.d4rkk.dev.nms.particle;

import org.bukkit.inventory.ItemStack;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ParticleOptionsItem implements ParticleOptions{

    private static final Class<?> particlesClass;
    private static final Class<?> itemParticleOptionClass;
    private static final Class<?> itemClass;

    static {
        try {
            particlesClass = Class.forName("net.minecraft.core.particles.ParticleTypes");
            itemParticleOptionClass = Class.forName("net.minecraft.core.particles.ItemParticleOption");
            itemClass = Class.forName("net.minecraft.world.item.Items");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private final ItemStack item;

    public ParticleOptionsItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public Object makeOption() {
        try {

            Field itemField = particlesClass.getField("ITEM");
            Object itemParticleType = itemField.get(null);


            Object itemStack = getItemStack();

            Method factory = itemParticleOptionClass.getMethod("a",
                    Class.forName("net.minecraft.core.particles.ParticleType"),
                    Class.forName("net.minecraft.world.item.ItemStack")
            );

            return factory.invoke(null, itemParticleType, itemStack);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar ItemParticleOption", e);
        }
    }


    public ItemStack getItem() {
        return item;
    }

    private Object getItemStack() {
        try {
            Class<?> itemsClass = Class.forName("net.minecraft.world.item.Items");
            Class<?> itemStackClass = Class.forName("net.minecraft.world.item.ItemStack");
            Class<?> holderClass = Class.forName("net.minecraft.core.Holder");

            Field field = itemsClass.getField(item.getType().name());
            Object holderItem = field.get(null);

            Constructor<?> ctor = itemStackClass.getConstructor(holderClass, int.class);
            return ctor.newInstance(holderItem, 1);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar NMS ItemStack", e);
        }
    }
}

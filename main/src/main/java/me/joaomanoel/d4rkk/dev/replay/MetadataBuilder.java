/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityPose
 *  com.comphenix.protocol.wrappers.WrappedDataWatcher
 *  org.bukkit.entity.Entity
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.lang.reflect.Method;
import org.bukkit.entity.Entity;

public class MetadataBuilder {
    private WrappedDataWatcher data;

    public MetadataBuilder(Entity en) {
        this.data = WrappedDataWatcher.getEntityWatcher((Entity)en).deepClone();
    }

    public MetadataBuilder(WrappedDataWatcher data) {
        this.data = data;
    }

    public MetadataBuilder setValue(int index, Object value) {
        this.data.setObject(index, value);
        return this;
    }

    public MetadataBuilder setInvisible() {
        return this.setValue(0, (byte)32);
    }

    public MetadataBuilder setCrouched() {
        return this.setValue(0, (byte)2);
    }

    public MetadataBuilder resetValue() {
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14)) {
            return this.setValue(0, (byte)0).setPoseField("STANDING");
        }
        return this.setValue(0, (byte)0);
    }

    public MetadataBuilder setArrows(int amount) {
        if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_10, VersionUtil.VersionEnum.V1_13)) {
            return this.setValue(10, amount);
        }
        if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_14, VersionUtil.VersionEnum.V1_16)) {
            return this.setValue(11, amount);
        }
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
            return this.setValue(12, amount);
        }
        return this.setValue(9, amount);
    }

    public MetadataBuilder setGlowing() {
        return this.setValue(0, (byte)32);
    }

    public MetadataBuilder setSilent() {
        return this.setValue(4, true);
    }

    public MetadataBuilder setNoGravity() {
        return this.setValue(5, true);
    }

    public MetadataBuilder setHealth(float amount) {
        return this.setValue(7, Float.valueOf(amount));
    }

    public MetadataBuilder setAir(int amount) {
        return this.setValue(1, amount);
    }

    public WrappedDataWatcher getData() {
        return this.data;
    }

    public MetadataBuilder setPoseField(String type) {

        Object enumField = null;
        try {
            Class<?> entityPose = Class.forName("net.minecraft.server." + VersionUtil.VERSION + ".EntityPose");
            Method valueOf = entityPose.getMethod("valueOf", String.class);
            enumField = valueOf.invoke(null, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setValue(6, enumField);
    }
}


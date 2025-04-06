/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityPose
 *  com.comphenix.protocol.wrappers.WrappedDataWatcher
 */
package me.joaomanoel.d4rkk.dev.replay;


import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.io.Serializable;


public class PlayerWatcher
implements Serializable {
    private static final long serialVersionUID = -5198365909032922108L;
    private boolean sneaking = false;
    private boolean burning = false;
    private boolean blocking = false;
    private boolean elytra = false;
    private boolean swimming = false;
    private String name;

    public PlayerWatcher(String name) {
        this.name = name;
    }

    public WrappedDataWatcher getMetadata(MetadataBuilder builder) {
        if (this.isValueActive()) {
            byte sneakByte = (byte)(this.sneaking ? 2 : 0);
            byte burnByte = (byte)(this.burning ? 1 : 0);
            int oldBlock = this.blocking ? 16 : 0;
            byte elytraByte = (byte)(this.elytra ? 128 : 0);
            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13)) {
                oldBlock = !VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_13) || !this.swimming ? 0 : 24;
            }
            byte value = (byte)(burnByte | sneakByte | oldBlock | elytraByte);
            builder.setValue(0, value);
            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_14)) {
                builder.setPoseField(this.getActivePose());
            }
        } else {
            builder.resetValue();
        }
        if (!VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            byte blockByte = (byte)(this.blocking ? 1 : 0);
            if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_10, VersionUtil.VersionEnum.V1_13)) {
                builder.setValue(6, blockByte);
            } else if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_14, VersionUtil.VersionEnum.V1_16)) {
                builder.setValue(7, blockByte);
            } else if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
                builder.setValue(8, blockByte);
            } else {
                builder.setValue(5, blockByte);
            }
        }
        return builder.getData();
    }

    private String getActivePose() {
        if (this.sneaking) {
            return VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_14) ? "SNEAKING" : EnumWrappers.PlayerAction.START_SNEAKING.toString();
        }
        if (this.swimming) {
            return  null;
        }
        return  null;
    }

    private boolean isValueActive() {
        return this.sneaking || this.blocking || this.burning || this.elytra || this.swimming;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public void setElytra(boolean elytra) {
        this.elytra = elytra;
    }

    public void setSwimming(boolean swimming) {
        this.swimming = swimming;
    }

    public boolean isBurning() {
        return this.burning;
    }

    public boolean isBlocking() {
        return this.blocking;
    }

    public boolean isElytra() {
        return this.elytra;
    }

    public boolean isSwimming() {
        return this.swimming;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public String getName() {
        return this.name;
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class SkinInfo
extends JsonClass {
    @SerializedName(value="id")
    private String id;
    @SerializedName(value="name")
    private String name;
    @SerializedName(value="properties")
    private List<Map<String, String>> properties;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Map<String, String>> getProperties() {
        return this.properties;
    }
}


/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.google.gson.Gson;

public class JsonData {
    private boolean enabled;
    private JsonClass jsonClass;
    private Gson gson = new Gson();
    private String data;

    public JsonData(boolean enabled) {
        this.enabled = enabled;
    }

    public JsonData(boolean enabled, JsonClass jsonClass) {
        this.enabled = enabled;
        this.jsonClass = jsonClass;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void convertData() {
        this.jsonClass = (JsonClass)this.gson.fromJson(this.data, this.jsonClass.getClass());
    }

    public JsonClass getJsonClass() {
        return this.jsonClass;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}


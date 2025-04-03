/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;


public class DatabaseRegistry {
    public static Database database;

    public static void registerDatabase(Database d) {
        database = d;
    }

    public static boolean isRegistered() {
        return database != null;
    }

    public static Database getDatabase() {
        return database;
    }
}


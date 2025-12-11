package me.joaomanoel.d4rkk.dev.database.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DatabaseCache {
    
    private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();
    private static final long DEFAULT_TTL = TimeUnit.MINUTES.toMillis(5); // 5 minutos
    
    public static void put(String key, Object value) {
        put(key, value, DEFAULT_TTL);
    }
    
    public static void put(String key, Object value, long ttlMillis) {
        CACHE.put(key, new CacheEntry(value, System.currentTimeMillis() + ttlMillis));
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        CacheEntry entry = CACHE.get(key);
        if (entry == null || entry.isExpired()) {
            CACHE.remove(key);
            return null;
        }
        return (T) entry.value;
    }
    
    public static void invalidate(String key) {
        CACHE.remove(key);
    }
    
    public static void invalidatePattern(String pattern) {
        CACHE.keySet().removeIf(key -> key.startsWith(pattern));
    }
    
    public static void clear() {
        CACHE.clear();
    }
    
    public static void cleanExpired() {
        CACHE.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    public static int size() {
        return CACHE.size();
    }
    
    private static class CacheEntry {
        final Object value;
        final long expiresAt;
        
        CacheEntry(Object value, long expiresAt) {
            this.value = value;
            this.expiresAt = expiresAt;
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }
}
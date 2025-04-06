/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package me.joaomanoel.d4rkk.dev.replay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebsiteFetcher {
    private static Gson gson = new GsonBuilder().create();
    private static Map<String, String> webChache = new HashMap<String, String>();
    private static Map<String, JsonClass> jsonCache = new HashMap<String, JsonClass>();
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private String content;

    public WebsiteFetcher(String content) {
        this.content = content;
    }

    public static void getContent(final String url, final boolean cache, Consumer<String> action) {
        pool.execute(new Acceptor<String>(action){

            @Override
            public String getValue() {
                return WebsiteFetcher.getContent(url, cache);
            }
        });
    }

    public static void getJson(final String url, final boolean cache, final JsonData jsonData, Consumer<JsonClass> action) {
        pool.execute(new Acceptor<JsonClass>(action){

            @Override
            public JsonClass getValue() {
                return WebsiteFetcher.getJson(url, cache, jsonData);
            }
        });
    }

    public static JsonClass getJson(String url, boolean cache, JsonData jsonData) {
        try {
            WebsiteFetcher data;
            if (cache && webChache.containsKey(url)) {
                data = (WebsiteFetcher)gson.fromJson(webChache.get(url), WebsiteFetcher.class);
            } else {
                HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
                connection.setReadTimeout(5000);
                StringBuilder sb = new StringBuilder();
                Scanner sc = new Scanner(new BufferedReader(new InputStreamReader(connection.getInputStream())));
                while (sc.hasNext()) {
                    sb.append(sc.nextLine()).append('\n');
                }
                data = new WebsiteFetcher(sb.toString());
            }
            webChache.put(url, gson.toJson(data));
            jsonData.setData(data.content);
            jsonData.convertData();
            jsonCache.put(url, jsonData.getJsonClass());
            return jsonData.getJsonClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getContent(String url, boolean cache) {
        try {
            WebsiteFetcher data;
            if (cache && webChache.containsKey(url)) {
                data = (WebsiteFetcher)gson.fromJson(webChache.get(url), WebsiteFetcher.class);
            } else {
                HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
                connection.setReadTimeout(5000);
                StringBuilder sb = new StringBuilder();
                Scanner sc = new Scanner(new BufferedReader(new InputStreamReader(connection.getInputStream())));
                while (sc.hasNext()) {
                    sb.append(sc.nextLine()).append('\n');
                }
                data = new WebsiteFetcher(sb.toString());
            }
            webChache.put(url, gson.toJson(data));
            return data.content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


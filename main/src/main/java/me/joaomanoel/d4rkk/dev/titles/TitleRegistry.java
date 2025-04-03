package me.joaomanoel.d4rkk.dev.titles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TitleRegistry {
    private static final List<Title> TITLES = new ArrayList<>();

    public static void register(Title title) {
        TITLES.add(title);
    }

    public static Title getById(String id) {
        return TITLES.stream()
                .filter(title -> title.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static Collection<Title> listTitles() {
        return new ArrayList<>(TITLES);
    }

    public static void clear() {
        TITLES.clear();
    }
}
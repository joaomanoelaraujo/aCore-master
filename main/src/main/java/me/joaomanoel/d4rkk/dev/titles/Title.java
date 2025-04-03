package me.joaomanoel.d4rkk.dev.titles;

import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class Title {
  private final String id;
  private final String title;
  private final String description;

  public Title(String id, String title, String description) {
    this.id = id;
    this.title = title;
    this.description = description;
  }

  public String getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public String getDescription() {
    return this.description;
  }

  public void give(Profile profile) {
    if (!this.has(profile)) {
      profile.getTitlesContainer().add(this);
    }
  }

  public boolean has(Profile profile) {
    return profile.getTitlesContainer().has(this);
  }

  public ItemStack getIcon(Profile profile) {
    return TitleIconFactory.createIcon(this, profile);
  }

  public static Collection<Title> listTitles() {
    return TitleRegistry.listTitles();
  }

  public static Title getById(String id) {
    return TitleRegistry.getById(id);
  }
}
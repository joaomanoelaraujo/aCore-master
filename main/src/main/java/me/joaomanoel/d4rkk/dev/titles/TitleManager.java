package me.joaomanoel.d4rkk.dev.titles;

import me.joaomanoel.d4rkk.dev.player.Profile;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class TitleManager {
  private static final TitleManager INSTANCE = new TitleManager();
  private final Map<String, TitleController> controllers = new HashMap<>();

  private TitleManager() {}

  public static TitleManager getInstance() {
    return INSTANCE;
  }

  public void handleJoinLobby(Profile profile) {
    TitleVisibilityHandler.handleJoinLobby(profile, controllers);
  }

  public void handleLeaveLobby(Profile profile) {
    TitleVisibilityHandler.handleLeaveLobby(profile, controllers);
  }

  public void handleLeaveServer(Profile profile) {
    TitleController controller = controllers.remove(profile.getName());
    if (controller != null) {
      controller.destroy();
    }
  }

  public void showTitle(Profile profile, Profile target) {
    TitleVisibilityHandler.showTitle(profile, target, getTitleController(target));
  }

  public void hideTitle(Profile profile, Profile target) {
    Player player = profile.getPlayer();
    TitleController controller = getTitleController(target);
    if (controller != null) {
      controller.hideToPlayer(player);
    }
  }

  public void selectTitle(Profile profile, Title title) {
    TitleSelectionHandler.selectTitle(profile, title, controllers);
  }

  public void deselectTitle(Profile profile) {
    TitleSelectionHandler.deselectTitle(profile, getTitleController(profile));
  }

  public TitleController getTitleController(Profile profile) {
    return controllers.get(profile.getName());
  }

  // Static facade methods for easier access
  public static void joinLobby(Profile profile) {
    getInstance().handleJoinLobby(profile);
  }

  public static void leaveLobby(Profile profile) {
    getInstance().handleLeaveLobby(profile);
  }

  public static void leaveServer(Profile profile) {
    getInstance().handleLeaveServer(profile);
  }

  public static void show(Profile profile, Profile target) {
    getInstance().showTitle(profile, target);
  }

  public static void hide(Profile profile, Profile target) {
    getInstance().hideTitle(profile, target);
  }

  public static void select(Profile profile, Title title) {
    getInstance().selectTitle(profile, title);
  }

  public static void deselect(Profile profile) {
    getInstance().deselectTitle(profile);
  }
}
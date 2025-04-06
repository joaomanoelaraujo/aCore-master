package me.joaomanoel.d4rkk.dev.titles;

import me.joaomanoel.d4rkk.dev.player.Profile;
import java.util.Map;

public class TitleSelectionHandler {
    
    public static void selectTitle(Profile profile, Title title, Map<String, TitleController> controllers) {
        TitleController controller = controllers.get(profile.getName());
        if (controller == null) {
            controller = new TitleController(profile.getPlayer(), title.getTitle());
            controller.enable();
            controllers.put(profile.getName(), controller);
            return;
        }
        
        controller.setName(title.getTitle());
    }
    
    public static void deselectTitle(Profile profile, TitleController controller) {
        if (controller == null) {
            return;
        }
        
        controller.setName("disabled");
    }
}
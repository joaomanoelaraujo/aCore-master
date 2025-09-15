package me.joaomanoel.d4rkk.dev.database.data.container;

import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.AbstractContainer;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import me.joaomanoel.d4rkk.dev.titles.Title;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class SelectedContainer extends AbstractContainer {

    public SelectedContainer(DataContainer dataContainer) {
        super(dataContainer);
        this.dataContainer.set(check(this.dataContainer.getAsJsonObject()).toString()); //builda e reseta o
    }

    public void setIcon(String id) {
        JSONObject selected = this.dataContainer.getAsJsonObject();
        selected.put("icon", id);
        this.dataContainer.set(selected.toString());
    }

    public Title getTitle() {
        return Title.getById(this.dataContainer.getAsJsonObject().get("title").toString());
    }

    public void setTitle(String id) {
        JSONObject selected = this.dataContainer.getAsJsonObject();
        selected.put("title", id);
        this.dataContainer.set(selected.toString());
    }

    public boolean isGlowed() {
        return ((boolean) this.dataContainer.getAsJsonObject().get("glow"));
    }

    public void changeGlow(Player player) {
        JSONObject selected = this.dataContainer.getAsJsonObject();
        boolean finalValue = !isGlowed();
        selected.put("glow", finalValue);
        this.dataContainer.set(selected.toJSONString());
        BukkitUtils.setGlow(player, finalValue);
    }

    private JSONObject check(JSONObject currentJson) {
        JSONObject defaultValue = new JSONObject();
        defaultValue.put("title", 0);
        defaultValue.put("icon", 0);
        defaultValue.put("glow", false);

        Set<String> keyNotFaound = new HashSet<>();
        defaultValue.keySet().forEach(key -> {
            if (!currentJson.containsKey(key)) keyNotFaound.add((String) key);
        });

        keyNotFaound.forEach(key -> currentJson.put(key, defaultValue.get(key)));

        return currentJson;
    }
}

package me.joaomanoel.d4rkk.dev.cosmetic.container;

import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.cosmetic.CosmeticType;
import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.AbstractContainer;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class SelectedContainer extends AbstractContainer {
  
  public SelectedContainer(DataContainer dataContainer) {
    super(dataContainer);
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    if (!cosmetics.containsKey("JOIN_MESSAGE")) {
      for (CosmeticType type : CosmeticType.values()) {
        cosmetics.put(type.name(), new JSONObject());
      }
    }
    
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public long getIndex(Cosmetic cosmetic) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    JSONObject selected = (JSONObject) cosmetics.get(cosmetic.getType().name());
    final long[] id = new long[1];
    selected.forEach((key, value) -> {
      if (value.equals(cosmetic.getId())) {
        id[0] = Long.parseLong(String.valueOf(key));
      }
    });
    return id[0];
  }
  
  public void setSelected(Cosmetic cosmetic) {
    this.setSelected(cosmetic.getType(), cosmetic.getId());
  }
  
  public void setSelected(CosmeticType type, long id) {
    this.setSelected(type, id, 1);
  }
  
  public void setSelected(CosmeticType type, long id, long index) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    ((JSONObject) cosmetics.get(type.name())).put(String.valueOf(index), id);
    this.dataContainer.set(cosmetics.toString());
    cosmetics.clear();
  }
  
  public boolean isSelected(Cosmetic cosmetic) {
    return this.isSelected(cosmetic, 1);
  }
  
  public boolean isSelected(Cosmetic cosmetic, long index) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    JSONObject selected = (JSONObject) cosmetics.get(cosmetic.getType().name());
    if (!selected.containsKey(String.valueOf(index))) {
      selected.put(String.valueOf(index), 0L);
      this.dataContainer.set(cosmetics.toString());
    }
    boolean isSelected = selected.get(String.valueOf(index)).equals(cosmetic.getId());
    selected.clear();
    cosmetics.clear();
    return isSelected;
  }
  
  public <T extends Cosmetic> T getSelected(CosmeticType type, Class<T> cosmeticClass) {
    return this.getSelected(type, cosmeticClass, 1);
  }
  
  public <T extends Cosmetic> T getSelected(CosmeticType type, Class<T> cosmeticClass, long index) {
    JSONObject cosmetics = this.dataContainer.getAsJsonObject();
    JSONObject selected = (JSONObject) cosmetics.get(type.name());
    if (!selected.containsKey(String.valueOf(index))) {
      selected.put(String.valueOf(index), 0L);
      this.dataContainer.set(cosmetics.toString());
    }
    T cosmetic = Cosmetic.findById(cosmeticClass, (long) selected.get(String.valueOf(index)));
    selected.clear();
    cosmetics.clear();
    return cosmetic;
  }
}
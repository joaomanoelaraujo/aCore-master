package me.joaomanoel.d4rkk.dev.database.data;

import me.joaomanoel.d4rkk.dev.database.data.interfaces.AbstractContainer;
import me.joaomanoel.d4rkk.dev.reflection.Accessors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class DataContainer {
  
  private Object value;
  private boolean updated;
  private Map<Class<? extends AbstractContainer>, AbstractContainer> containerMap = new HashMap<>();
  
  public DataContainer(Object value) {
    this.value = value;
  }
  
  public void gc() {
    this.value = null;
    this.containerMap.values().forEach(AbstractContainer::gc);
    this.containerMap.clear();
    this.containerMap = null;
  }
  
  public void set(Object value) {
    if (this.value == null || !this.value.equals(value)) {
      this.setUpdated(true);
    }
    this.value = value;
  }
  
  public void addInt(int amount) {
    this.set(getAsInt() + amount);
  }
  
  public void addLong(long amount) {
    this.set(getAsLong() + amount);
  }
  
  public void addDouble(double amount) {
    this.set(getAsDouble() + amount);
  }
  
  public void removeInt(int amount) {
    this.set(getAsInt() - amount);
  }
  
  public void removeLong(long amount) {
    this.set(getAsLong() - amount);
  }
  
  public void removeDouble(double amount) {
    this.set(getAsDouble() - amount);
  }
  
  public Object get() {
    return this.value;
  }
  
  public int getAsInt() {
    return Integer.parseInt(this.getAsString());
  }
  
  public long getAsLong() {
    return Long.parseLong(this.getAsString());
  }
  
  public double getAsDouble() {
    return Double.parseDouble(this.getAsString());
  }
  
  public String getAsString() {
    return this.value.toString();
  }
  
  public boolean getAsBoolean() {
    return Boolean.parseBoolean(this.getAsString());
  }
  
  public JSONObject getAsJsonObject() {
    try {
      return (JSONObject) new JSONParser().parse(this.getAsString());
    } catch (Exception ex) {
      throw new IllegalArgumentException("\"" + value + "\" is not a JsonObject: ", ex);
    }
  }
  
  public JSONArray getAsJsonArray() {
    try {
      return (JSONArray) new JSONParser().parse(this.getAsString());
    } catch (Exception ex) {
      throw new IllegalArgumentException("\"" + value + "\" is not a JsonArray: ", ex);
    }
  }
  
  public boolean isUpdated() {
    return this.updated;
  }
  
  public void setUpdated(boolean updated) {
    this.updated = updated;
  }
  
  @SuppressWarnings("unchecked")
  public <T extends AbstractContainer> T getContainer(Class<T> containerClass) {
    if (!this.containerMap.containsKey(containerClass)) {
      this.containerMap.put(containerClass, Accessors.getConstructor(containerClass, DataContainer.class).newInstance(this));
    }
    
    return (T) this.containerMap.get(containerClass);
  }
}

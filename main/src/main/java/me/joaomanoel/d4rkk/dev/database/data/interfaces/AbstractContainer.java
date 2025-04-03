package me.joaomanoel.d4rkk.dev.database.data.interfaces;

import me.joaomanoel.d4rkk.dev.database.data.DataContainer;

public abstract class AbstractContainer {
  
  protected DataContainer dataContainer;
  
  public AbstractContainer(DataContainer dataContainer) {
    this.dataContainer = dataContainer;
  }
  
  public void gc() {
    this.dataContainer = null;
  }
}

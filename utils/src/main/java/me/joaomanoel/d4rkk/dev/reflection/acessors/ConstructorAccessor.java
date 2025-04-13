package me.joaomanoel.d4rkk.dev.reflection.acessors;

import java.lang.reflect.Constructor;

public class ConstructorAccessor<T> {
  private final Constructor<T> constructor;

  @SuppressWarnings("unchecked")
  public ConstructorAccessor(Constructor<?> constructor) {
    this.constructor = (Constructor<T>) constructor;
    this.constructor.setAccessible(true);
  }

  public T newInstance(Object... args) {
    try {
      return constructor.newInstance(args);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create new instance", e);
    }
  }

  public Constructor<T> getHandle() {
    return constructor;
  }
}
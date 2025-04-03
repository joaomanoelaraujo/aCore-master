package me.joaomanoel.d4rkk.dev.reflection.acessors;

import java.lang.reflect.Method;

public class MethodAccessor {

  private final Method method;

  public MethodAccessor(Method method) {
    this.method = method;
    this.method.setAccessible(true); // Tornar o método acessível
  }

  public Object invoke(Object instance, Object... args) {
    try {
      return method.invoke(instance, args);
    } catch (Exception e) {
      throw new RuntimeException("Failed to invoke method: " + method.getName(), e);
    }
  }

  public Method getHandle() {
    return method;
  }
}

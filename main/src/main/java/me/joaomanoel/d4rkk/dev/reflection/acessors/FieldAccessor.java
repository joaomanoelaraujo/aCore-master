package me.joaomanoel.d4rkk.dev.reflection.acessors;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldAccessor<T> {

  private final Field field;

  public FieldAccessor(Field field) {
    this.field = field;

    // Tornar o campo acessível, se necessário
    if (!field.isAccessible()) {
      field.setAccessible(true);
    }

    // Remover o modificador FINAL se necessário para versões antigas
    if (Modifier.isFinal(field.getModifiers())) {
      try {
        // Ajuste para versões mais antigas de Java
        if (field.getModifiers() == (field.getModifiers() & ~Modifier.FINAL)) {
          if (java.lang.reflect.AccessibleObject.class.isAssignableFrom(field.getClass())) {
            java.lang.reflect.AccessibleObject.setAccessible(new java.lang.reflect.AccessibleObject[]{field}, true);
          }
        }
      } catch (Exception e) {
        throw new RuntimeException("Failed to remove final modifier", e);
      }
    }
  }

  public void set(Object instance, T value) {
    try {
      field.set(instance, value);
    } catch (Exception e) {
      throw new RuntimeException("Failed to set field: " + field.getName(), e);
    }
  }

  @SuppressWarnings("unchecked")
  public T get(Object instance) {
    try {
      return (T) field.get(instance);
    } catch (Exception e) {
      throw new RuntimeException("Failed to get field: " + field.getName(), e);
    }
  }

  public Field getHandle() {
    return field;
  }
}

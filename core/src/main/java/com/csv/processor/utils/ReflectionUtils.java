package com.csv.processor.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

  public static <T> T createNewInstanceByDefaultConstructor(Class<T> tClass) {
    try {
      return tClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> Object getValue(T obj, Class<T> tClass, String field) {
    try {
      var declaredField = tClass.getDeclaredField(field);

      declaredField.setAccessible(true);

      return declaredField.get(obj);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new RuntimeException();
    }
  }

  public static <T> void setValue(T obj, Class<T> tClass, String field, Object value) {
    try {
      var declaredField = tClass.getDeclaredField(field);

      declaredField.setAccessible(true);

      declaredField.set(obj, value);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new RuntimeException();
    }
  }

  public static Field[] getDeclaredFields(Class<?> cls) {
    return cls.getDeclaredFields();
  }

}

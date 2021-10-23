package com.csv.processor.impl;

import com.csv.processor.Deserializer;
import com.csv.processor.impl.serialize.BooleanDeserializer;
import com.csv.processor.impl.serialize.DoubleDeserializer;
import com.csv.processor.impl.serialize.IntegerDeserializer;
import com.csv.processor.impl.serialize.StringDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultDeserializer implements Deserializer {
  private static final Map<Class<?>, Deserializer> DEFAULT_SERIALIZERS = new HashMap<>();

  static {
    DEFAULT_SERIALIZERS.put(String.class, new StringDeserializer());
    DEFAULT_SERIALIZERS.put(Double.class, new DoubleDeserializer());
    DEFAULT_SERIALIZERS.put(Integer.class, new IntegerDeserializer());
    DEFAULT_SERIALIZERS.put(Boolean.class, new BooleanDeserializer());
  }

  @Override
  public Object deserialize(String fromText, Class<?> type) {
    if (type.isPrimitive()) {
      type = mapPrimitiveTypeToWrapper(type);
    }
    var deserializer = DEFAULT_SERIALIZERS.get(type);

    if (Objects.isNull(deserializer)) {
      throw new IllegalArgumentException("Unable to find matching deserializer for given type");
    }
    return deserializer.deserialize(fromText, type);
  }

  private Class<?> mapPrimitiveTypeToWrapper(Class<?> type) {
    if (int.class.equals(type)) {
      return Integer.class;
    }
    else if (long.class.equals(type)) {
      return Long.class;
    }
    else if (char.class.equals(type)) {
      return Character.class;
    }
    else if (double.class.equals(type)) {
      return Double.class;
    }
    else if (float.class.equals(type)) {
      return Float.class;
    }
    else if (boolean.class.equals(type)) {
      return Boolean.class;
    }
    throw new IllegalStateException();
  }

}

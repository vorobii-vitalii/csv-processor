package com.csv.processor.impl.serialize;

import com.csv.processor.Deserializer;

public class BooleanDeserializer implements Deserializer {
  @Override
  public Object deserialize(String fromText, Class<?> type) {
    return Boolean.parseBoolean(fromText);
  }
}

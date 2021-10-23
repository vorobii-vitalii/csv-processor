package com.csv.processor.impl.serialize;

import com.csv.processor.Deserializer;

public class StringDeserializer implements Deserializer {
  @Override
  public Object deserialize(String fromText, Class<?> type) {
    return String.valueOf(fromText);
  }
}

package com.csv.processor.impl.serialize;

import com.csv.processor.Deserializer;

public class DoubleDeserializer implements Deserializer {
  @Override
  public Object deserialize(String fromText, Class<?> type) {
    return Double.parseDouble(fromText);
  }
}

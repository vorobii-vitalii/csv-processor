package com.csv.processor.impl.serialize;

import com.csv.processor.Deserializer;

import java.util.Optional;

public class IntegerDeserializer implements Deserializer {
  @Override
  public Object deserialize(String fromText, Class<?> type) {
    return Optional.ofNullable(fromText).map(Integer::parseInt).orElse(null);
  }
}

package com.csv.processor;

public interface Deserializer {
  Object deserialize(String fromText, Class<?> type);
}

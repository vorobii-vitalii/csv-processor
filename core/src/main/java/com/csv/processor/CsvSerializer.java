package com.csv.processor;

import com.csv.processor.impl.CsvSerializerImpl;

import java.util.List;
import java.util.function.BiConsumer;

public interface CsvSerializer {

  static CsvSerializer create() {
    return new CsvSerializerImpl();
  }

  static CsvSerializer create(Character delimiter) {
    return new CsvSerializerImpl(delimiter);
  }

  <T> void serializeSingle(T obj, Class<T> tClass, BiConsumer<String, Boolean> consumer);

  <T> void serializeMany(List<T> objects, Class<T> tClass, BiConsumer<String, Boolean> consumer);
}

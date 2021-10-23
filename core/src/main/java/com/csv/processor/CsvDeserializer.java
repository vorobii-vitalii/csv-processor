package com.csv.processor;

import com.csv.processor.impl.CsvDeserializerImpl;
import com.csv.processor.impl.CsvTokenizerImpl;

import java.util.List;

public interface CsvDeserializer<T> {

  static <R> CsvDeserializer<R> createDefault(Class<R> tClass) {
    return new CsvDeserializerImpl<>(tClass);
  }

  static <R> CsvDeserializer<R> createDefault(Class<R> tClass, Character delimiterCharacter) {
    return new CsvDeserializerImpl<>(new CsvTokenizerImpl(delimiterCharacter), tClass);
  }

  List<T> deserialize(String input, boolean isHeaderPresent);
}

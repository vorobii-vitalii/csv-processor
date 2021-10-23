package com.csv.processor.impl;

import com.csv.processor.CsvSerializer;
import com.csv.processor.Serializer;
import com.csv.processor.annotations.CsvField;
import com.csv.processor.annotations.Escape;
import com.csv.processor.annotations.Serialize;
import com.csv.processor.annotations.Transient;
import com.csv.processor.utils.ReflectionUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class CsvSerializerImpl implements CsvSerializer {

  private static final Character DELIMITER = ';';
  private static final int DEFAULT_POSITION = -1;
  private static final DefaultSerializer DEFAULT_SERIALIZER = new DefaultSerializer();

  private final Character delimiter;

  public CsvSerializerImpl() {
    this.delimiter = DELIMITER;
  }

  public CsvSerializerImpl(Character delimiter) {
    if (delimiter == null) {
      throw new RuntimeException("delimiter cannot be null");
    }
    this.delimiter = delimiter;
  }

  @Override
  public <T> void serializeSingle(T obj, Class<T> tClass, BiConsumer<String, Boolean> consumer) {
    var entries = getEntries(tClass);

    var headerRow = getHeaderRow(entries);
    consumer.accept(headerRow, true);

    var serializedEntityRow = getSerializedEntityRow(obj, tClass, entries);

    consumer.accept(serializedEntityRow, false);
  }

  @Override
  public <T> void serializeMany(List<T> objects, Class<T> tClass, BiConsumer<String, Boolean> consumer) {
    var entries = getEntries(tClass);

    var headerRow = getHeaderRow(entries);
    consumer.accept(headerRow, true);

    objects.forEach(obj -> {
      var serializedEntityRow = getSerializedEntityRow(obj, tClass, entries);

      consumer.accept(serializedEntityRow, false);
    });
  }

  private <T> String getSerializedEntityRow(T obj, Class<T> tClass, List<Entry> entries) {
    return entries.stream()
      .map(pair -> {
        var fieldName = pair.fieldName();
        var value = ReflectionUtils.getValue(obj, tClass, fieldName);
        var surroundChar = pair.surroundChar();

        if (surroundChar == null) {
          return pair.serializer().serialize(value);
        }
        return surroundChar.toString() + pair.serializer().serialize(value) + surroundChar;
      })
      .collect(Collectors.joining(delimiter.toString()));
  }

  private String getHeaderRow(List<Entry> entries) {
    return entries.stream()
      .map(Entry::columnName)
      .collect(Collectors.joining(delimiter.toString()));
  }

  private <T> List<Entry> getEntries(Class<T> tClass) {
    return Arrays.stream(ReflectionUtils.getDeclaredFields(tClass))
      .map(field -> {
        var csvFieldAnnotation = field.getAnnotation(CsvField.class);
        var escapeAnnotation = field.getAnnotation(Escape.class);
        var serializeAnnotation = field.getAnnotation(Serialize.class);
        var transientAnnotation = field.getAnnotation(Transient.class);

        if (transientAnnotation != null) {
          return null;
        }

        var columnName = csvFieldAnnotation != null ? csvFieldAnnotation.name() : field.getName();
        var priority = csvFieldAnnotation != null ? csvFieldAnnotation.position() : DEFAULT_POSITION;

        var serializer = serializeAnnotation != null
          ? ReflectionUtils.createNewInstanceByDefaultConstructor(serializeAnnotation.value())
          : DEFAULT_SERIALIZER;

        var escapeText = escapeAnnotation != null ? escapeAnnotation.value() : null;

        return new Entry(columnName, field.getName(), priority, serializer, escapeText);
      })
      .filter(Objects::nonNull)
      .sorted(Comparator.comparingInt(Entry::priority).thenComparing(Entry::columnName))
      .collect(Collectors.toList());
  }

  private record Entry(
    String columnName,
    String fieldName,
    Integer priority,
    Serializer serializer,
    Character surroundChar
  ) {
  }

}

package com.csv.processor.impl;

import com.csv.processor.*;
import com.csv.processor.annotations.CsvField;
import com.csv.processor.annotations.Deserialize;
import com.csv.processor.annotations.Transient;
import com.csv.processor.exception.InvalidTokensException;
import com.csv.processor.exception.ParseException;
import com.csv.processor.parse.CsvTokenizer;
import com.csv.processor.parse.Token;
import com.csv.processor.parse.TokenType;
import com.csv.processor.utils.CollectionUtils;
import com.csv.processor.utils.ReflectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CsvDeserializerImpl<T> implements CsvDeserializer<T> {
  private static final Deserializer DEFAULT_DESERIALIZER = new DefaultDeserializer();

  private final CsvTokenizer csvTokenizer;
  private final Class<T> entityClass;

  public CsvDeserializerImpl(Class<T> entityClass) {
    csvTokenizer = new CsvTokenizerImpl();
    this.entityClass = entityClass;
  }

  public CsvDeserializerImpl(CsvTokenizer csvTokenizer, Class<T> entityClass) {
    this.csvTokenizer = csvTokenizer;
    this.entityClass = entityClass;
  }

  @Override
  public List<T> deserialize(String input, boolean isHeaderPresent) {
    var tokens = csvTokenizer.tokenize(input);

    if (Objects.isNull(tokens)) {
      throw new InvalidTokensException();
    }
    var tokenLists = CollectionUtils.split(tokens, this::isNewLine);

    var entries = Arrays.stream(ReflectionUtils.getDeclaredFields(entityClass))
      .map(field -> {
        var csvFieldAnnotation = field.getAnnotation(CsvField.class);
        var deserializerAnnotation = field.getAnnotation(Deserialize.class);
        var transientAnnotation = field.getAnnotation(Transient.class);
        var fieldName = field.getName();

        if (transientAnnotation != null) {
          return null;
        }
        var columnName = csvFieldAnnotation != null ? csvFieldAnnotation.name() : fieldName;
        var columnPosition = csvFieldAnnotation != null ? csvFieldAnnotation.position() : -1;
        var deserializer = deserializerAnnotation != null
          ? ReflectionUtils.createNewInstanceByDefaultConstructor(deserializerAnnotation.value())
          : DEFAULT_DESERIALIZER;

        return new Entry(fieldName, columnName, columnPosition, deserializer, field.getType());
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toMap(Entry::columnName, Function.identity()));

    if (isHeaderPresent) {
      if (tokenLists.isEmpty()) {
        throw new ParseException();
      }
      var headerColumns = tokenLists.get(0).stream()
        .map(Token::value)
        .map(String::valueOf)
        .collect(Collectors.toList());

      var headerColumnsSize = headerColumns.size();

      return tokenLists.stream()
        .skip(1)
        .map(tokenList -> {
          if (tokenList.size() != headerColumnsSize) {
            throw new ParseException();
          }
          final T newInstance = ReflectionUtils.createNewInstanceByDefaultConstructor(entityClass);
          IntStream.range(0, headerColumnsSize)
            .forEach(columnIndex -> {
              var headerColumn = headerColumns.get(columnIndex);
              var value = tokenList.get(columnIndex).value();
              var entry = entries.get(headerColumn);

              if (entry == null) {
                throw new ParseException();
              }
              var deserializedValue =
                entry
                  .deserializer()
                  .deserialize(String.valueOf(value), entry.type());

              ReflectionUtils.setValue(newInstance, entityClass, entry.fieldName(), deserializedValue);
            });
          return newInstance;
        })
        .collect(Collectors.toList());
    }
    var entryListSorted = new ArrayList<>(entries.values());

    entryListSorted.sort(Comparator.comparingInt(Entry::position).thenComparing(Entry::columnName));

    var entryListSize = entryListSorted.size();

    return tokenLists.stream()
      .map(tokenList -> {
        if (tokenList.size() != entryListSize) {
          throw new ParseException();
        }
        var tokenValues = tokenList.stream().map(Token::value).collect(Collectors.toList());

        final T newInstance = ReflectionUtils.createNewInstanceByDefaultConstructor(entityClass);

        IntStream.range(0, entryListSize)
          .forEach(entryIndex -> {
            var entry = entryListSorted.get(entryIndex);
            var value = tokenValues.get(entryIndex);

            var deserializedValue = entry.deserializer().deserialize(String.valueOf(value), entry.type());

            ReflectionUtils.setValue(newInstance, entityClass, entry.fieldName(), deserializedValue);
          });

        return newInstance;
      })
      .collect(Collectors.toList());
  }

  private boolean isNewLine(Token token) {
    return TokenType.NEW_LINE.equals(token.tokenType());
  }

  private static record Entry(
    String fieldName,
    String columnName,
    Integer position,
    Deserializer deserializer,
    Class<?> type
  ) { }

}

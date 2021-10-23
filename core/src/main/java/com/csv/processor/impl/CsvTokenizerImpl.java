package com.csv.processor.impl;

import com.csv.processor.parse.CsvTokenizer;
import com.csv.processor.parse.Token;
import com.csv.processor.parse.TokenType;

import java.util.ArrayList;
import java.util.List;

public class CsvTokenizerImpl implements CsvTokenizer {

  private static final Character DEFAULT_DELIMITER = ';';
  private static final Character SINGLE_QUOTE = '\'';
  private static final Character DOUBLE_QUOTE = '"';
  private static final Character NEW_LINE = '\n';

  private final Character delimiter;

  public CsvTokenizerImpl() {
    delimiter = DEFAULT_DELIMITER;
  }

  public CsvTokenizerImpl(Character delimiter) {
    if (delimiter == null) {
      throw new RuntimeException("delimiter cannot be null");
    }
    this.delimiter = delimiter;
  }

  @Override
  public List<Token> tokenize(String input) {
    final int N = input.length();

    var tokens = new ArrayList<Token>();

    for (int i = 0; i < N; i++) {
      var c = input.charAt(i);

      if (c == SINGLE_QUOTE || c == DOUBLE_QUOTE) {
        var position = findSymbolOnSameLine(input, i + 1, c);

        if (position < 0) {
          throw new RuntimeException();
        }
        var escapedValue = input.substring(i + 1, position);

        tokens.add(new Token(escapedValue, TokenType.VALUE, i + 1));
        i = position;
      }
      else if (c == NEW_LINE) {
        tokens.add(new Token(null, TokenType.NEW_LINE, i));
      }
      else {
        if (Character.isWhitespace(c) || c == delimiter) {
          continue;
        }
        var delimiterPosition = findSymbolOnSameLine(input, i + 1, delimiter);

        if (delimiterPosition > 0) {
          var value = input.substring(i, delimiterPosition).trim();

          tokens.add(new Token(value, TokenType.VALUE, i));
          i = delimiterPosition - 1;
        }
        else {
          var newLinePosition = findSymbolOnSameLine(input, i + 1, NEW_LINE);

          if (newLinePosition > 0) {
            var value = input.substring(i, newLinePosition).trim();

            tokens.add(new Token(value, TokenType.VALUE, i));

            i = newLinePosition - 1;
          } else {
            var value = input.substring(i, N).trim();

            tokens.add(new Token(value, TokenType.VALUE, i));
            break;
          }
        }
      }

    }
    return tokens;
  }

  private int findSymbolOnSameLine(String input, int startIndex, Character quoteChar) {
    final int N = input.length();

    for (int i = startIndex; i < N; i++) {
      final char c = input.charAt(i);
      if (c == quoteChar) {
        return i;
      } else if (c == '\n') {
        return -1;
      }
    }
    return -1;
  }

}

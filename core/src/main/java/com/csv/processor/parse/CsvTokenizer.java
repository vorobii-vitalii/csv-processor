package com.csv.processor.parse;

import java.util.List;

public interface CsvTokenizer {
  List<Token> tokenize(String input);
}

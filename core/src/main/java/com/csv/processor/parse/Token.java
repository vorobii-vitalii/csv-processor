package com.csv.processor.parse;

public record Token(Object value, TokenType tokenType, Integer position) {
}

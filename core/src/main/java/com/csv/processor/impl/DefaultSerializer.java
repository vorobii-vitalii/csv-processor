package com.csv.processor.impl;

import com.csv.processor.Serializer;

/**
 * Default implementation relies on Object#toString
 */
public class DefaultSerializer implements Serializer {

  @Override
  public CharSequence serialize(Object object) {
    return String.valueOf(object);
  }

}

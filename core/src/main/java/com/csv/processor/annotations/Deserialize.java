package com.csv.processor.annotations;

import com.csv.processor.Deserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Deserialize {
  Class<? extends Deserializer> value();
}

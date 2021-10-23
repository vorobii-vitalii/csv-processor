package com.csv.processor.annotations;

import com.csv.processor.impl.DefaultSerializer;
import com.csv.processor.Serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Serialize {
  Class<? extends Serializer> value() default DefaultSerializer.class;
}

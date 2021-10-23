package com.csv.processor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CollectionUtils {

  public static <R> List<List<R>> split(List<R> list, Predicate<R> splitByPredicate) {
    List<List<R>> lists = new ArrayList<>();

    List<R> accumulateList = new ArrayList<R>();

    for (R e : list) {
      if (splitByPredicate.test(e)) {
        if (!accumulateList.isEmpty()) {
          lists.add(accumulateList);
          accumulateList = new ArrayList<>();
        }
      } else {
        accumulateList.add(e);
      }
    }
    return lists;
  }

}

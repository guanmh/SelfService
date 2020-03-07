package com.gmh.utils;

import com.google.common.primitives.Longs;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-01-21 16:57
 * @description: 集合类工具类
 */
public final class CollectionUtils {

  /**
   * 是否有交集
   *
   * @param coll1
   * @param coll2
   * @return
   */
  public static <T> boolean containsAny(Collection<T> coll1, T... coll2) {
    return org.apache.commons.collections4.CollectionUtils.containsAny(coll1, coll2);
  }

  /**
   * 比较数组中是否包含元素
   *
   * @return
   */
  public static boolean containsAny(Object[] objects, Object object) {
    for (int i = 0, size = CollectionUtils.size(objects); i < size; i++) {
      if (com.google.common.base.Objects.equal(objects[i], object)) {
        return true;
      }
    }
    return false;
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
    return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
  }

  public static <T> T first(Collection<T> collection) {
    return get(collection, 0);
  }

  /**
   * 获取数组、集合的下标所在元素
   *
   * @param object
   * @param i
   * @return
   */
  public static <T> T get(Object object, int i) {
    if (object == null) {
      return null;
    }
    return (T) org.apache.commons.collections4.CollectionUtils.get(object, i);
  }

  public static <T> T get(Collection<T> collection, int i) {
    int size = CollectionUtils.size(collection);
    if (size > 0 && size > i) {
      return IteratorUtils.get(collection.iterator(), i);
    }
    return null;
  }

  public static <T> T get(Object object, int i, Class<T> tClass) {
    if (Objects.isNull(object)) {
      return null;
    }
    Object o = org.apache.commons.collections4.CollectionUtils.get(object, i);
    if (Objects.isNull(o)) {
      return null;
    }
    return (T) o;
  }

  public static boolean isEmpty(Collection collection) {
    return org.apache.commons.collections4.CollectionUtils.isEmpty(collection);
  }

  public static boolean isEmpty(Map map) {
    return map == null || map.isEmpty();
  }

  public static boolean isEmpty(Object[] objects) {
    return size(objects) == 0;
  }

  public static boolean isNotEmpty(Collection collection) {
    return org.apache.commons.collections4.CollectionUtils.isNotEmpty(collection);
  }

  public static boolean isNotEmpty(Map map) {
    return !isEmpty(map);
  }

  public static boolean isNotEmpty(Object[] objects) {
    return !isEmpty(objects);
  }

  public static <T> T last(Collection<T> collection) {
    if (isNotEmpty(collection)) {
      return get(collection, size(collection) - 1);
    }
    return null;
  }

  public static int size(Object object) {
    return org.apache.commons.collections4.CollectionUtils.size(object);
  }

  /**
   * 把id,id的字符串分割得到List集合
   *
   * @param idStr
   * @param split
   * @return
   */
  public static List<Long> splitId(String idStr, String split) {
    String[] splitArray = StringUtils.split(idStr, split);
    if (CollectionUtils.isEmpty(splitArray)) {
      return null;
    } else {
      return Arrays.stream(splitArray)
          .map(Longs::tryParse)
          .filter(Objects::nonNull)
          .distinct()
          .collect(Collectors.toList());
    }
  }

  public static <T> T[] toArray(Collection<T> ids) {
    int size = CollectionUtils.size(ids);
    if (size == 0) {
      return null;
    }
    T[] ts = (T[]) Array.newInstance(first(ids).getClass(), size);
    return ids.toArray(ts);
  }
}

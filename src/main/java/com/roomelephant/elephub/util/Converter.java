package com.roomelephant.elephub.util;

/**
 * A generic interface for converting an object of type {@code O} to an object of type {@code T}.
 *
 * @param <T> the target type after conversion
 * @param <O> the original source type to be converted
 */
public interface Converter<T, O> {

  /**
   * Converts an object of type {@code O} into an object of type {@code T}.
   *
   * @param original the source object to convert
   * @return the converted object of type {@code T}
   */
  T convert(O original);
}

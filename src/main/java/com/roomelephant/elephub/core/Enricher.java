package com.roomelephant.elephub.core;

/**
 * Defines a contract for enriching an internal object using data from an external source object.
 *
 * <p>Implementations of this interface are responsible for mapping or transferring specific
 * parts of the external object's data into the internal structure.
 * </p>
 *
 * @param <E> the type of the external (source) object
 * @param <I> the type of the internal (target) object
 */
public interface Enricher<E, I> {

  /**
   * Enriches the internal object using information from the external object.
   *
   * @param externalObject the source object from an external library or system
   * @param internalObject the target internal object to be enriched
   */
  void enrich(E externalObject, I internalObject);
}

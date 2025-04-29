package com.roomelephant.elephub.application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EnrichmentChain<E, I> {

  private final List<Enricher<E, I>> enrichers = new ArrayList<>();

  public EnrichmentChain<E, I> addEnricher(Enricher<E, I> enricher) {
    enrichers.add(enricher);
    return this;
  }

  public I enrich(E externalObject, Supplier<I> internalSupplier) {
    I internalObject = internalSupplier.get();
    for (Enricher<E, I> enricher : enrichers) {
      enricher.enrich(externalObject, internalObject);
    }
    return internalObject;
  }
}

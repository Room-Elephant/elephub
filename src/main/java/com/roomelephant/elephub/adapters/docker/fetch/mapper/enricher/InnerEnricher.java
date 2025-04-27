package com.roomelephant.elephub.adapters.docker.fetch.mapper.enricher;

import com.roomelephant.elephub.adapters.docker.fetch.mapper.Enricher;
import com.roomelephant.elephub.container.Container;

public class InnerEnricher implements Enricher<com.github.dockerjava.api.model.Container, Container.ContainerBuilder> {
  @Override
  public void enrich(com.github.dockerjava.api.model.Container external, Container.ContainerBuilder builder) {
    builder.inner(external);
  }
}
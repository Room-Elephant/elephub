package com.roomelephant.elephub.adapters.docker.fetch.mapper.enricher;

import com.roomelephant.elephub.adapters.docker.fetch.mapper.Enricher;
import com.roomelephant.elephub.container.Container;
import com.roomelephant.elephub.container.State;
import java.time.Instant;
import java.util.Arrays;

public class BasicInformationEnricher implements Enricher<com.github.dockerjava.api.model.Container,
    Container.ContainerBuilder> {

  @Override
  public void enrich(com.github.dockerjava.api.model.Container external, Container.ContainerBuilder builder) {
    builder.created(Instant.ofEpochSecond(external.getCreated()));
    builder.image(external.getImage());
    builder.names(Arrays.asList(external.getNames()));
    builder.state(State.valueOf(external.getState().toUpperCase()));
    builder.ports(Arrays.stream(external.getPorts())
        .filter(containerPort -> containerPort.getPublicPort() != null)
        .map(containerPort -> String.valueOf(containerPort.getPublicPort()))
        .toList());
  }
}
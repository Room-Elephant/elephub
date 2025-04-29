package com.roomelephant.elephub.adapters.docker.fetch.mapper;

import com.roomelephant.elephub.container.Container;
import com.roomelephant.elephub.container.State;
import com.roomelephant.elephub.util.Converter;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DockerContainerToContainer implements Converter<Container, com.github.dockerjava.api.model.Container> {
  public Container convert(com.github.dockerjava.api.model.Container dockerContainer) {
    return com.roomelephant.elephub.container.Container.builder()
        .inner(dockerContainer)
        .created(Instant.ofEpochSecond(dockerContainer.getCreated()))
        .image(dockerContainer.getImage())
        .names(List.copyOf(Arrays.asList(dockerContainer.getNames())))
        .state(State.valueOf(dockerContainer.getState().toUpperCase()))
        .ports(Arrays.stream(dockerContainer.getPorts())
            .filter(containerPort -> containerPort.getPublicPort() != null)
            .map(containerPort -> String.valueOf(containerPort.getPublicPort()))
            .toList())
        .labels(Map.copyOf(dockerContainer.getLabels()))
        .build();
  }
}

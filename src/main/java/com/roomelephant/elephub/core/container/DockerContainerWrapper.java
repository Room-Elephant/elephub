package com.roomelephant.elephub.core.container;

import com.roomelephant.elephub.core.BaseDockerInformation;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
public final class DockerContainerWrapper implements BaseDockerInformation {
  @ToString.Exclude
  private final com.github.dockerjava.api.model.Container inner;

  public DockerContainerWrapper(com.github.dockerjava.api.model.Container dockerContainer) {
    if (dockerContainer == null) {
      throw new IllegalArgumentException("dockerContainer is null");
    }
    this.inner = dockerContainer;
  }

  @Override
  public Instant getCreated() {
    return Instant.ofEpochSecond(inner.getCreated());
  }

  @Override
  public String getImage() {
    return inner.getImage();
  }

  @Override
  public List<String> getNames() {
    return Arrays.asList(inner.getNames());
  }

  @Override
  public State getState() {
    return State.valueOf(inner.getState().toUpperCase());
  }

  @Override
  public List<Port> getPorts() {
    return Arrays.stream(inner.getPorts())
        .filter(containerPort -> containerPort.getPublicPort() != null)
        .map(containerPort ->
            new Port(containerPort.getPublicPort(), containerPort.getPrivatePort()))
        .toList();
  }

  @Override
  public Map<String, String> getLabels() {
    return Map.copyOf(inner.getLabels());
  }

  public String toString() {
    return "Container(created=" + this.getCreated() + ", image=" + this.getImage() + ", names=" + this.getNames()
        + ", state=" + this.getState() + ", ports=" + this.getPorts() + ", labels=" + this.getLabels() + ")";
  }


}


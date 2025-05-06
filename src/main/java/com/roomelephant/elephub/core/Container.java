package com.roomelephant.elephub.core;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
public final class Container {
  @ToString.Exclude
  private final com.github.dockerjava.api.model.Container inner;

  public Container(com.github.dockerjava.api.model.Container inner) {
    if (inner == null) {
      throw new IllegalArgumentException("inner is null");
    }
    this.inner = inner;
  }

  public Instant getCreated() {
    return Instant.ofEpochSecond(inner.getCreated());
  }

  public String getImage() {
    return inner.getImage();
  }

  public List<String> getNames() {
    return Arrays.asList(inner.getNames());
  }

  public State getState() {
    return State.valueOf(inner.getState().toUpperCase());
  }

  public List<Port> getPorts() {
    return Arrays.stream(inner.getPorts())
        .filter(containerPort -> containerPort.getPublicPort() != null)
        .map(containerPort ->
            new Container.Port(containerPort.getPublicPort(), containerPort.getPrivatePort()))
        .toList();
  }

  public Map<String, String> getLabels() {
    return Map.copyOf(inner.getLabels());
  }

  public String toString() {
    return "Container(created=" + this.getCreated() + ", image=" + this.getImage() + ", names=" + this.getNames()
        + ", state=" + this.getState() + ", ports=" + this.getPorts() + ", labels=" + this.getLabels() + ")";
  }

  public record Port(Integer publicPort, Integer privatePort) {
  }

  public enum State {
    CREATED, RESTARTING, RUNNING, REMOVING, PAUSED, EXITED, DEAD
  }

}


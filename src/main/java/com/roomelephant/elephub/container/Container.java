package com.roomelephant.elephub.container;

import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ExcludeFromJacocoGeneratedReport
@Builder
@ToString
@EqualsAndHashCode
public final class Container implements BaseDockerInformation {
  @ToString.Exclude
  private final com.github.dockerjava.api.model.Container inner;

  private final Instant created;
  private final String image;
  private final List<String> names;
  private final State state;
  private final List<String> ports;
  private final Map<String, String> labels;

  @Override
  public Instant getCreated() {
    return created;
  }

  @Override
  public String getImage() {
    return image;
  }

  @Override
  public List<String> getNames() {
    return names;
  }

  @Override
  public State getState() {
    return state;
  }

  @Override
  public List<String> getPorts() {
    return ports;
  }

  @Override
  public Map<String, String> getLabels() {
    return labels;
  }
}

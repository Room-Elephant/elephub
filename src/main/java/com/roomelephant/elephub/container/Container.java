package com.roomelephant.elephub.container;

import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ExcludeFromJacocoGeneratedReport
@Builder
@ToString
@EqualsAndHashCode
public class Container implements BaseDockerInformation {
  @ToString.Exclude
  private final com.github.dockerjava.api.model.Container inner;

  private Instant created;
  private String image;
  private List<String> names;
  private State state;
  private List<String> ports;

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
}

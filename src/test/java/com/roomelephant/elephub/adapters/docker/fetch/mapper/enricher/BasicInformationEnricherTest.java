package com.roomelephant.elephub.adapters.docker.fetch.mapper.enricher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.model.ContainerPort;
import com.roomelephant.elephub.container.Container;
import com.roomelephant.elephub.container.State;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicInformationEnricherTest {

  public static final long EPOCH = 1714220000L;
  public static final String IMAGE = "test-image:latest";
  public static final String NAME = "test-container";
  public static final String STATE = "running";
  public static final int PORT1 = 8080;
  public static final int PORT2 = 9090;

  private final BasicInformationEnricher victim = new BasicInformationEnricher();

  @Mock
  private com.github.dockerjava.api.model.Container externalContainer;

  @Mock
  private ContainerPort containerPort1;

  @Mock
  private ContainerPort containerPort2;

  @Mock
  private ContainerPort containerPort3;

  @BeforeEach
  void setUp() {
    when(externalContainer.getCreated()).thenReturn(EPOCH);
    when(externalContainer.getImage()).thenReturn(IMAGE);
    when(externalContainer.getNames()).thenReturn(new String[] {NAME});
    when(externalContainer.getState()).thenReturn(STATE);

    when(containerPort1.getPublicPort()).thenReturn(PORT1);
    when(containerPort2.getPublicPort()).thenReturn(PORT2);
    when(containerPort3.getPublicPort()).thenReturn(null);
    when(externalContainer.getPorts()).thenReturn(new ContainerPort[] {containerPort1, containerPort2, containerPort3});
  }

  @Test
  void shouldEnrichBuilderWithContainerInformation() {
    Container.ContainerBuilder builder = Container.builder();

    victim.enrich(externalContainer, builder);
    Container result = builder.build();

    assertEquals(Instant.ofEpochSecond(EPOCH), result.getCreated());
    assertEquals(IMAGE, result.getImage());
    assertEquals(List.of(NAME), result.getNames());
    assertEquals(State.RUNNING, result.getState());
    assertEquals(List.of(String.valueOf(PORT1), String.valueOf(PORT2)), result.getPorts());
    verify(externalContainer).getCreated();
    verify(externalContainer).getImage();
    verify(externalContainer).getNames();
    verify(externalContainer).getState();
    verify(externalContainer).getPorts();
    verify(containerPort1, times(2)).getPublicPort();
    verify(containerPort2, times(2)).getPublicPort();
    verify(containerPort3, times(1)).getPublicPort();
  }
}
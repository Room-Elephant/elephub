package com.roomelephant.elephub.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.model.ContainerPort;
import com.roomelephant.elephub.core.container.DockerContainerWrapper;
import com.roomelephant.elephub.core.container.Port;
import com.roomelephant.elephub.core.container.State;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DockerContainerWrapperTest {

  private static final long EPOCH = 1714220000L;
  private static final String IMAGE = "test-image:latest";
  private static final String NAME = "test-container";
  private static final String STATE = "running";
  private static final int PORT1 = 8080;
  private static final int PORT2 = 9090;
  private static final Map<String, String> LABELS = Map.of("KEY", "VALUE");

  private DockerContainerWrapper victim;

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
    victim = new DockerContainerWrapper(externalContainer);
  }

  @Test
  void shouldGetCreated() {
    when(externalContainer.getCreated()).thenReturn(EPOCH);

    assertEquals(Instant.ofEpochSecond(EPOCH), victim.getCreated());
    verify(externalContainer).getCreated();
  }

  @Test
  void shouldGetImage() {
    when(externalContainer.getImage()).thenReturn(IMAGE);

    assertEquals(IMAGE, victim.getImage());
    verify(externalContainer).getImage();
  }

  @Test
  void shouldGetNames() {
    when(externalContainer.getNames()).thenReturn(new String[] {NAME});

    assertEquals(List.of(NAME), victim.getNames());
    verify(externalContainer).getNames();
  }

  @Test
  void shouldGetState() {
    when(externalContainer.getState()).thenReturn(STATE);

    assertEquals(State.RUNNING, victim.getState());
    verify(externalContainer).getState();
  }

  @Test
  void shouldGetPorts() {
    when(externalContainer.getPorts()).thenReturn(new ContainerPort[] {containerPort1, containerPort2, containerPort3});
    when(containerPort1.getPublicPort()).thenReturn(PORT1);
    when(containerPort1.getPrivatePort()).thenReturn(PORT1);
    when(containerPort2.getPublicPort()).thenReturn(PORT2);
    when(containerPort2.getPrivatePort()).thenReturn(PORT2);
    when(containerPort3.getPublicPort()).thenReturn(null);

    assertEquals(List.of(new Port(PORT1, PORT1), new Port(PORT2, PORT2)),
        victim.getPorts());
    verify(externalContainer).getPorts();
    verify(containerPort1, times(2)).getPublicPort();
    verify(containerPort2, times(2)).getPublicPort();
    verify(containerPort3, times(1)).getPublicPort();
  }

  @Test
  void shouldGetLabels() {
    when(externalContainer.getLabels()).thenReturn(LABELS);

    assertEquals(LABELS, victim.getLabels());
    verify(externalContainer).getLabels();
  }

  @Test
  void shouldThrowExceptionOnNullExternalContainer() {
    IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        new DockerContainerWrapper(null)
    );

    assertEquals("dockerContainer is null", exception.getMessage());
  }

  @Test
  void shouldTestToString() {
    when(externalContainer.getCreated()).thenReturn(EPOCH);
    when(externalContainer.getImage()).thenReturn(IMAGE);
    when(externalContainer.getNames()).thenReturn(new String[] {NAME});
    when(externalContainer.getState()).thenReturn(STATE);
    when(externalContainer.getPorts()).thenReturn(new ContainerPort[] {containerPort1, containerPort2, containerPort3});
    when(externalContainer.getLabels()).thenReturn(LABELS);
    when(containerPort1.getPublicPort()).thenReturn(PORT1);
    when(containerPort1.getPrivatePort()).thenReturn(PORT1);
    when(containerPort2.getPublicPort()).thenReturn(PORT2);
    when(containerPort2.getPrivatePort()).thenReturn(PORT2);
    when(containerPort3.getPublicPort()).thenReturn(null);

    assertEquals("Container("
            + "created=2024-04-27T12:13:20Z, "
            + "image=test-image:latest, "
            + "names=[test-container], "
            + "state=RUNNING, "
            + "ports=[Port[publicPort=8080, privatePort=8080], Port[publicPort=9090, privatePort=9090]], "
            + "labels={KEY=VALUE})",
        victim.toString());
  }

}
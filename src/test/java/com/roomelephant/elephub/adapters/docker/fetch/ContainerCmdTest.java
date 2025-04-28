package com.roomelephant.elephub.adapters.docker.fetch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.roomelephant.elephub.container.Container;
import com.roomelephant.elephub.util.Converter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContainerCmdTest {

  @Mock
  DockerClient dockerClient;

  @Mock
  Converter<Container, com.github.dockerjava.api.model.Container> converter;

  @Mock
  ListContainersCmd listContainersCmd;

  @Mock
  com.github.dockerjava.api.model.Container dockerContainer;

  @Mock
  Container container;

  ContainerCmd victim;

  @BeforeEach
  void setUp() {
    victim = new ContainerCmd(dockerClient, converter);
  }

  @Test
  void shouldReturnEnrichedContainers() {
    when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
    when(listContainersCmd.exec()).thenReturn(List.of(dockerContainer));

    when(converter.convert(dockerContainer)).thenReturn(container);

    List<Container> result = victim.fetch();

    assertEquals(1, result.size());
    assertEquals(container, result.getFirst());

    verify(dockerClient).listContainersCmd();
    verify(listContainersCmd).exec();
    verify(converter).convert(any());
  }
}
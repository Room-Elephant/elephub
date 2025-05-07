package com.roomelephant.elephub.external.docker.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.roomelephant.elephub.core.container.DockerContainerWrapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DockerCommandTest {

  @Mock
  DockerClient dockerClient;

  @Mock
  ListContainersCmd listContainersCmd;

  @Mock
  com.github.dockerjava.api.model.Container dockerContainer;

  @Mock
  DockerContainerWrapper dockerContainerWrapper;

  DockerCommand victim;

  @BeforeEach
  void setUp() {
    victim = new DockerCommand(dockerClient);
  }

  @Test
  void shouldReturnDecoratedContainers() {
    when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
    when(listContainersCmd.exec()).thenReturn(List.of(dockerContainer));

    List<DockerContainerWrapper> result = victim.getContainers();

    assertEquals(1, result.size());
    assertEquals(DockerContainerWrapper.class, result.getFirst().getClass());

    verify(dockerClient).listContainersCmd();
    verify(listContainersCmd).exec();
  }
}
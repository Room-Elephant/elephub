package com.roomelephant.elephub.external.docker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.external.docker.command.DockerCommand;
import com.roomelephant.elephub.external.docker.connect.DockerClientFactory;
import com.roomelephant.elephub.external.docker.connect.PostValidations;
import com.roomelephant.elephub.external.docker.connect.PreValidations;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;

class DockerFacadeTest {

  @Mock
  DockerClient mockClient;

  @Test
  void shouldInitiateClientAndReturnContainers() {
    String dockerPath = "/some/path/docker";

    try (
        MockedConstruction<PreValidations> mockedPre = mockConstruction(PreValidations.class);
        MockedConstruction<PostValidations> mockedPost = mockConstruction(PostValidations.class);
        MockedConstruction<DockerClientFactory> mockedFactory = mockConstruction(DockerClientFactory.class,
            (factory, context) -> {
              when(factory.getDockerClient(dockerPath)).thenReturn(mockClient);
            });
        MockedConstruction<DockerCommand> mockedCmd = mockConstruction(DockerCommand.class,
            (cmd, context) -> {
              when(cmd.getContainers()).thenReturn(Collections.emptyList());
            })
    ) {
      DockerFacade facade = new DockerFacade();
      facade.initiateDockerClient(dockerPath);

      assertEquals(Collections.emptyList(), facade.getContainers());
      DockerCommand createdCommand = mockedCmd.constructed().getFirst();
      verify(createdCommand, times(1)).getContainers();
      DockerClientFactory createdFactory = mockedFactory.constructed().getFirst();
      verify(createdFactory, times(1)).getDockerClient(dockerPath);
    }
  }
}
package com.roomelephant.elephub.adapters.docker.connect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.core.DockerClientImpl;
import com.roomelephant.elephub.adapters.docker.connect.exceptions.DockerExceptionConnection;
import com.roomelephant.elephub.adapters.docker.connect.path.DockerPathValidation;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DockerClientFactoryTest {

  @Mock
  private DockerPathValidation dockerPathValidation;

  @Mock
  private DockerClient dockerClient;

  @Mock
  private PingCmd pingCmd;

  private MockedStatic<DockerClientImpl> dockerClientImplMockedStatic;

  private DockerClientFactory victim;


  @BeforeEach
  void setUp() {
    victim = new DockerClientFactory(dockerPathValidation);
    dockerClientImplMockedStatic = Mockito.mockStatic(DockerClientImpl.class);
    dockerClientImplMockedStatic.when(() -> DockerClientImpl.getInstance(any(), any())).thenReturn(dockerClient);
  }

  @AfterEach
  void tearDown() {
    if (dockerClientImplMockedStatic != null) {
      dockerClientImplMockedStatic.close();
    }
  }

  @Test
  void shouldThrowExceptionWhenPathValidationFails() {
    String dockerHost = "/var/run/docker.sock";
    when(dockerPathValidation.validate(any(Path.class))).thenReturn(false);

    assertThrows(DockerExceptionConnection.class, () -> victim.getDockerClient(dockerHost));
    verify(dockerPathValidation).validate(any(Path.class));
  }

  @Test
  void shouldThrowExceptionWhenPingFails() {
    when(dockerPathValidation.validate(any(Path.class))).thenReturn(true);
    when(dockerClient.pingCmd()).thenReturn(pingCmd);
    when(pingCmd.exec()).thenThrow(new RuntimeException("Connection failed"));
    String dockerHost = "/var/run/docker.sock";

    assertThrows(DockerExceptionConnection.class, () -> victim.getDockerClient(dockerHost));
    verify(dockerPathValidation).validate(any(Path.class));
    verify(dockerClient).pingCmd();
    verify(pingCmd).exec();
  }

  @Test
  void shouldReturnDockerClientWhenEverythingSucceeds() throws DockerExceptionConnection {
    String dockerHost = "/var/run/docker.sock";
    when(dockerPathValidation.validate(any(Path.class))).thenReturn(true);
    when(dockerClient.pingCmd()).thenReturn(pingCmd);

    DockerClient result = victim.getDockerClient(dockerHost);

    assertEquals(dockerClient, result);
    verify(dockerPathValidation).validate(any(Path.class));
    verify(dockerClient).pingCmd();
    verify(pingCmd).exec();
  }
}
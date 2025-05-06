package com.roomelephant.elephub.external.docker.connect;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PingCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostValidationsTest {
  @Mock
   DockerClient dockerClient;

  @Mock
   PingCmd pingCmd;

  PostValidations victim;

  @BeforeEach
  void setUp() {
    victim = new PostValidations();
  }

  @Test
  void shouldThrowExceptionWhenValidationPasses() throws DockerConnectionException {
    when(dockerClient.pingCmd()).thenReturn(pingCmd);
    doNothing().when(pingCmd).exec();

    victim.validate(dockerClient);
    verify(dockerClient).pingCmd();
  }

  @Test
  void shouldThrowExceptionWhenValidationFails() {
    doThrow(RuntimeException.class).when(dockerClient).pingCmd();

    assertThrows(DockerConnectionException.class, () -> victim.validate(dockerClient));
    verify(dockerClient).pingCmd();
  }
}
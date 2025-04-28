package com.roomelephant.elephub.adapters.docker.connect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientImpl;
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

  static final String PATH = "/var/run/docker.sock";
  @Mock
  Validations<String> preValidations;
  @Mock
  Validations<DockerClient> postValidations;

  @Mock
  DockerClient dockerClient;

  MockedStatic<DockerClientImpl> dockerClientImplMockedStatic;

  DockerClientFactory victim;

  @BeforeEach
  void setUp() {
    victim = new DockerClientFactory(preValidations, postValidations);
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
  void shouldReturnDockerClientWhenEverythingSucceeds() throws DockerConnectionException {
    doNothing().when(preValidations).validate(PATH);
    doNothing().when(postValidations).validate(dockerClient);

    DockerClient result = victim.getDockerClient(PATH);

    assertEquals(dockerClient, result);
    verify(preValidations).validate(PATH);
    verify(postValidations).validate(dockerClient);
  }

  @Test
  void shouldThrowExceptionWhenPreValidationFails() throws DockerConnectionException {
    doThrow(DockerConnectionException.class).when(preValidations).validate(PATH);

    assertThrows(DockerConnectionException.class, () -> victim.getDockerClient(PATH));
    verify(preValidations).validate(PATH);
    verifyNoInteractions(postValidations);
  }

  @Test
  void shouldThrowExceptionWhenPostValidationFails() throws DockerConnectionException {
    doNothing().when(preValidations).validate(PATH);
    doThrow(DockerConnectionException.class).when(postValidations).validate(dockerClient);

    assertThrows(DockerConnectionException.class, () -> victim.getDockerClient(PATH));
    verify(preValidations).validate(PATH);
    verify(postValidations).validate(dockerClient);
  }

  @Test
  void shouldThrowExceptionWhenClientCreationFails() throws DockerConnectionException {
    doNothing().when(preValidations).validate(PATH);
    dockerClientImplMockedStatic.when(() -> DockerClientImpl.getInstance(any(), any()))
        .thenThrow(RuntimeException.class);

    assertThrows(DockerConnectionException.class, () -> victim.getDockerClient(PATH));
    verify(preValidations).validate(PATH);
    verifyNoInteractions(postValidations);
  }

  @Test
  void shouldReturnTheSameDockerClientOnSuccessiveCalls() throws DockerConnectionException {
    doNothing().when(preValidations).validate(PATH);
    doNothing().when(postValidations).validate(dockerClient);

    DockerClient result = victim.getDockerClient(PATH);
    DockerClient result2 = victim.getDockerClient(PATH);

    assertEquals(dockerClient, result);
    assertEquals(result, result2);
    verify(preValidations, times(2)).validate(PATH);
    verify(postValidations, times(1)).validate(dockerClient);
    dockerClientImplMockedStatic.verify(() -> DockerClientImpl.getInstance(any(), any()), times(1));
  }

  @Test
  void shouldComputeOnDifferentDockerHostsCalls() throws DockerConnectionException {
    String secondHost = "second-host";
    doNothing().when(preValidations).validate(PATH);
    doNothing().when(preValidations).validate(secondHost);
    doNothing().when(postValidations).validate(dockerClient);

    victim.getDockerClient(PATH);
    victim.getDockerClient(secondHost);

    verify(preValidations, times(2)).validate(anyString());
    verify(postValidations, times(2)).validate(dockerClient);
    dockerClientImplMockedStatic.verify(() -> DockerClientImpl.getInstance(any(), any()), times(2));
  }
}
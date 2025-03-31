package com.roomelephant.elephub.adapters.docker.connect;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.roomelephant.elephub.adapters.docker.connect.exceptions.DockerExceptionConnection;
import com.roomelephant.elephub.adapters.docker.connect.path.DockerPathValidation;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerClientFactory {
  private final DockerPathValidation dockerPathValidation;

  public DockerClientFactory(DockerPathValidation dockerPathValidation) {
    this.dockerPathValidation = dockerPathValidation;
  }

  public DockerClient getDockerClient(String dockerHost) throws DockerExceptionConnection {
    Path socketPath = Path.of(dockerHost);

    boolean valid = dockerPathValidation.validate(socketPath);
    if (!valid) {
      throw new DockerExceptionConnection();
    }

    DockerClientConfig config = DefaultDockerClientConfig
        .createDefaultConfigBuilder()
        .withDockerHost("unix://" + dockerHost)
        .withDockerTlsVerify(false)
        .build();

    DockerHttpClient httpClient = new OkDockerHttpClient
        .Builder()
        .dockerHost(config.getDockerHost())
        .build();

    DockerClient client;
    try {
      client = DockerClientImpl.getInstance(config, httpClient);

      log.debug("operation='getDockerClient', message='Attempting to connect to Docker daemon...'");
      client.pingCmd().exec();
    } catch (Exception e) {
      log.error("operation='getDockerClient', message='Failed to connect to Docker daemon: {}'",
          e.getMessage(), e);
      throw new DockerExceptionConnection(e);
    }

    return client;
  }
}

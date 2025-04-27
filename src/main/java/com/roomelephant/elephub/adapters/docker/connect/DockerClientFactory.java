package com.roomelephant.elephub.adapters.docker.connect;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerClientFactory {
  private final Validations<String> preValidations;
  private final Validations<DockerClient> postValidations;
  private final ConcurrentMap<String, DockerClient> clientCache = new ConcurrentHashMap<>();

  public DockerClientFactory(Validations<String> preValidations, Validations<DockerClient> postValidations) {
    this.preValidations = preValidations;
    this.postValidations = postValidations;
  }

  public DockerClient getDockerClient(String dockerHost) throws DockerConnectionException {
    preValidations.validate(dockerHost);
    return clientCache.computeIfAbsent(dockerHost, host -> {
      DockerClient client = createDockerClient(dockerHost);
      postValidations.validate(client);
      return client;
    });
  }

  private DockerClient createDockerClient(String dockerHost) throws DockerConnectionException {
    try {
      DockerClientConfig config = createDockerClientConfig(dockerHost);
      DockerHttpClient httpClient = createDockerHttpClient(config);

      return DockerClientImpl.getInstance(config, httpClient);
    } catch (Exception e) {
      log.error("operation='createDockerClient', message='Failed to create docker client': {}'",
          e.getMessage(), e);
      throw new DockerConnectionException("Could not create docker client", e);
    }
  }

  private DockerHttpClient createDockerHttpClient(DockerClientConfig config) {
    return new OkDockerHttpClient
        .Builder()
        .dockerHost(config.getDockerHost())
        .build();
  }

  private DockerClientConfig createDockerClientConfig(String dockerHost) {
    return DefaultDockerClientConfig
        .createDefaultConfigBuilder()
        .withDockerHost("unix://" + dockerHost)
        .withDockerTlsVerify(false)
        .build();
  }
}

package com.roomelephant.elephub.adapters.docker.connect;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.adapters.docker.connect.config.DockerConfig;
import com.roomelephant.elephub.adapters.docker.connect.exceptions.DockerExceptionConnection;
import com.roomelephant.elephub.adapters.docker.connect.path.DockerPathValidation;
import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import lombok.Getter;

//TODO: create tests
@ExcludeFromJacocoGeneratedReport
public class Docker {
  @Getter
  private static final Docker instance = new Docker();

  @Getter
  private DockerClient dockerClient = null;

  private Docker() {
    init();
  }

  private void init() {
    DockerConfig dockerConfig = new DockerConfig();

    DockerPathValidation pathValidation = new DockerPathValidation(dockerConfig.pathValidationsRules());
    DockerClientFactory factory = new DockerClientFactory(pathValidation);

    try {
      dockerClient = factory.getDockerClient(dockerConfig.dockerPath());
    } catch (DockerExceptionConnection e) {
      throw new RuntimeException(new DockerExceptionConnection());
    }
  }
}

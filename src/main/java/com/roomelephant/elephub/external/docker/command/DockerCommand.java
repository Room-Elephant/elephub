package com.roomelephant.elephub.external.docker.command;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.core.container.DockerContainerWrapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerCommand implements com.roomelephant.elephub.core.container.DockerCmd {
  private final DockerClient dockerClient;

  public DockerCommand(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  @Override
  public List<DockerContainerWrapper> getContainers() {
    return dockerClient.listContainersCmd().exec().stream()
        .map(DockerContainerWrapper::new)
        .toList();
  }
}

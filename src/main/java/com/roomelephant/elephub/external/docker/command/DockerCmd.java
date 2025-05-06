package com.roomelephant.elephub.external.docker.command;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.core.Container;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerCmd {
  private final DockerClient dockerClient;

  public DockerCmd(DockerClient dockerClient) {
    this.dockerClient = dockerClient;
  }

  public List<Container> getContainers() {
    return dockerClient.listContainersCmd().exec().stream()
        .map(Container::new)
        .toList();
  }
}

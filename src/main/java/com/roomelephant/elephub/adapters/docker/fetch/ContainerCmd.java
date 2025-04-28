package com.roomelephant.elephub.adapters.docker.fetch;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.container.Container;
import com.roomelephant.elephub.util.Converter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContainerCmd {
  private final DockerClient dockerClient;
  private final Converter<Container, com.github.dockerjava.api.model.Container> converter;

  public ContainerCmd(DockerClient dockerClient,
                      Converter<Container, com.github.dockerjava.api.model.Container> converter) {
    this.dockerClient = dockerClient;
    this.converter = converter;
  }

  public List<Container> fetch() {
    return dockerClient.listContainersCmd().exec().stream()
        .map(converter::convert)
        .toList();
  }
}

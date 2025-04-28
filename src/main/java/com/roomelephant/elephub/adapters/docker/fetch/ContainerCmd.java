package com.roomelephant.elephub.adapters.docker.fetch;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.adapters.docker.fetch.mapper.EnrichmentChain;
import com.roomelephant.elephub.container.Container;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContainerCmd {
  private final DockerClient dockerClient;
  private final EnrichmentChain<com.github.dockerjava.api.model.Container, Container.ContainerBuilder> enricher;

  public ContainerCmd(DockerClient dockerClient,
                      EnrichmentChain<com.github.dockerjava.api.model.Container, Container.ContainerBuilder> enricher) {
    this.dockerClient = dockerClient;
    this.enricher = enricher;
  }

  public List<Container> fetch() {
    return dockerClient.listContainersCmd().exec().stream()
        .map(dockerContainer ->
            enricher.enrich(dockerContainer, Container::builder).build()
        )
        .toList();
  }
}

package com.roomelephant.elephub.core.container;

import java.util.List;

/**
 * Interface for executing Docker-related commands.
 */
public interface DockerCmd {

  /**
   * Retrieves a list of running Docker containers.
   *
   * @return a list of {@link DockerContainerWrapper} objects representing the running containers.
   */
  List<DockerContainerWrapper> getContainers();
}
package com.roomelephant.elephub.core.container;

/**
 * Interface for creating docker clients.
 */
public interface DockerCmdClient {

  /**
   * Initializes the Docker client with the specified Docker host.
   *
   * @param dockerHost the URI or address of the Docker host to connect to (e.g., "unix:///var/run/docker.sock" or "tcp://localhost:2375").
   */
  void initiateDockerClient(String dockerHost);
}
package com.roomelephant.elephub.container;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Represents basic information retrieved from a Docker container.
 * This interface provides access to common attributes of a Docker container.
 * The data is directly derived from the Docker API's Container information.
 */
public sealed interface BaseDockerInformation permits Container {
  /**
   * Retrieves the creation timestamp of the Docker container.
   *
   * @return An {@link Instant} representing the creation time of the container in UTC.
   */
  Instant getCreated();

  /**
   * Retrieves the image used to create the Docker container.
   *
   * @return The image name
   */
  String getImage();

  /**
   * Retrieves the list of names assigned to the Docker container.
   *
   * @return A {@link List} of Strings representing the container's names.
   */
  List<String> getNames();

  /**
   * Retrieves the current state of the Docker container.
   *
   * @return The {@link State} of the container.
   */
  State getState();

  /**
   * Retrieves the list of exposed public getPorts exposed by the Docker container,
   * on tcp protocol.
   *
   * @return A {@link List} of Strings representing the exposed getPorts. Example: ["3000"].
   */
  List<String> getPorts();

  /**
   * Retrieves the label of the Docker container grouped by domain
   *
   * @return An {@link Map} representing the labels.
   */
  Map<String, String> getLabels();
}

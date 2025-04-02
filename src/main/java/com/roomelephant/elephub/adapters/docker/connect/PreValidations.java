package com.roomelephant.elephub.adapters.docker.connect;

import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreValidations implements Validations<String> {
  private final List<ValidationRule<Path>> rules = List.of(
      new ValidationRule<>(
          socketPath -> socketPath.toFile().exists(),
          "Docker socket not found"),
      new ValidationRule<>(
          socketPath -> socketPath.toFile().canRead() && socketPath.toFile().canWrite(),
          "Insufficient permissions to access Docker socket")
  );

  @Override
  public void validate(String dockerHost) throws DockerConnectionException {
    if (dockerHost == null || dockerHost.isBlank()) {
      throw new DockerConnectionException(dockerHost, "Invalid path");
    }

    Path socketPath = Path.of(dockerHost);

    for (var rule : rules) {
      if (!rule.validator().test(socketPath)) {
        log.error(
            "operation='validate', message='{}' socket='{}', user='{}', permissions='{can_read: {}, can_write: {}}'",
            rule.errorMessage(), dockerHost, System.getProperty("user.name"),
            socketPath.toFile().canRead(), socketPath.toFile().canWrite());

        throw new DockerConnectionException(dockerHost, rule.errorMessage());
      }
    }
  }
}

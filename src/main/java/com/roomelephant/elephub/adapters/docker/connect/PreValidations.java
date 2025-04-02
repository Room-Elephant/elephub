package com.roomelephant.elephub.adapters.docker.connect;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreValidations implements Validations<String> {
  private static final List<ValidationRule<String>> stringRules = List.of(
      new ValidationRule<>(
          Objects::nonNull,
          "Docker socket cannot be null"),
      new ValidationRule<>(
          p -> !p.isBlank(),
          "Docker socket cannot be blank")
  );

  private static final List<ValidationRule<Path>> pathRules = List.of(
      new ValidationRule<>(
          socketPath -> socketPath.toFile().exists(),
          "Docker socket not found"),
      new ValidationRule<>(
          socketPath -> socketPath.toFile().canRead() && socketPath.toFile().canWrite(),
          "Insufficient permissions to access Docker socket")
  );

  @Override
  public void validate(String dockerHost) throws DockerConnectionException {
    testRules(stringRules, dockerHost);

    Path socketPath = Path.of(dockerHost);

    testRules(pathRules, socketPath);
  }

  private static <T> void testRules(List<ValidationRule<T>> rules, T path) throws DockerConnectionException {
    for (var rule : rules) {
      if (!rule.validator().test(path)) {
        String parsedPath = path == null ? null : path.toString();
        boolean canRead = path instanceof Path p && p.toFile().canRead();
        boolean canWrite = path instanceof Path p && p.toFile().canWrite();
        handleFailure(rule.errorMessage(), parsedPath, canRead, canWrite);
      }
    }
  }

  private static void handleFailure(String rule, String dockerHost, boolean canRead, boolean canWrite)
      throws DockerConnectionException {
    log.error(
        "operation='handleFailure', message='{}' socket='{}', user='{}', permissions='{can_read: {}, can_write: {}}'",
        rule, dockerHost, System.getProperty("user.name"),
        canRead, canWrite);

    throw new DockerConnectionException(dockerHost, rule);
  }

  private record ValidationRule<T>(Predicate<T> validator, String errorMessage) {
  }
}

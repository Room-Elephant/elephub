package com.roomelephant.elephub.adapters.docker.connect.path;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;

public class Validations {
  private Validations() {
  }

  public static final Predicate<Path> HAS_PATH = Objects::nonNull;

  public static final Predicate<Path> HAS_VALID_PATH = (socketPath) -> socketPath.toFile().exists();

  public static final Predicate<Path> HAS_VALID_PERMISSIONS = (socketPath) ->
      socketPath.toFile().canRead() && socketPath.toFile().canWrite();


}

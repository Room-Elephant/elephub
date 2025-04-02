package com.roomelephant.elephub.adapters.docker.connect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PreValidationsTest {

  @TempDir
  static Path tempDir;

  @TempDir
  static Path invalidPath;

  @TempDir
  static Path validPath;

  PreValidations victim;

  @BeforeAll
  static void setUpAll() throws IOException {
    validPath.toFile().setReadable(true);
    validPath.toFile().setWritable(true);
    Files.delete(invalidPath);
  }

  @BeforeEach
  void setUp() {
    victim = new PreValidations();
  }

  @Test
  void shouldDoNothingOnValidPath() throws DockerConnectionException {
    victim.validate(validPath.toString());
  }

  @ParameterizedTest(name = "{2}")
  @MethodSource("providePathTestCases")
  void shouldThrowOnInvalidPath(Path path, String errorMessage, String testCase) {
    DockerConnectionException dockerConnectionException =
        assertThrows(DockerConnectionException.class, () -> victim.validate(path == null ? null : path.toString()));

    assertEquals(errorMessage, dockerConnectionException.getMessage());
  }

  @ParameterizedTest(name = "{2}")
  @MethodSource("providePermissionTestCases")
  void testPathPermissions(boolean readable, boolean writable, String testCase) {
    tempDir.toFile().setReadable(readable);
    tempDir.toFile().setWritable(writable);
    String message = "Insufficient permissions to access Docker socket" + ": '" + tempDir + "'";


    DockerConnectionException dockerConnectionException =
        assertThrows(DockerConnectionException.class, () -> victim.validate(tempDir.toString()));

    assertEquals(message, dockerConnectionException.getMessage());
  }

  private static Stream<Arguments> providePathTestCases() {
    return Stream.of(
        Arguments.of(null, "Docker socket cannot be null: 'null'", "null path"),
        Arguments.of("", "Docker socket cannot be blank: ''", "blank path"),
        Arguments.of(invalidPath, "Docker socket not found: '" + invalidPath + "'", "invalid path")
    );
  }

  private static Stream<Arguments> providePermissionTestCases() {
    return Stream.of(
        Arguments.of(true, false, "Only read permission"),
        Arguments.of(false, true, "Only write permission"),
        Arguments.of(false, false, "No permissions")
    );
  }
}
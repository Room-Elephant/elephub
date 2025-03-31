package com.roomelephant.elephub.adapters.docker.connect.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ValidationsTest {

  @TempDir
  static Path tempDir;

  @TempDir
  static Path invalidPath;

  @TempDir
  static Path validPath;

  @BeforeAll
  static void setUp() throws IOException {
    Files.delete(invalidPath);
    validPath.toFile().setReadable(true);
    validPath.toFile().setWritable(true);
  }

  @ParameterizedTest(name = "{2}")
  @MethodSource("providePathTestCases")
  void testHasPath(Path path, boolean expectedResult, String testCase) {
    boolean result = Validations.HAS_PATH.test(path);
    assertEquals(expectedResult, result);
  }

  @ParameterizedTest(name = "{2}")
  @MethodSource("provideValidPathTestCases")
  void testValidPath(Path path, boolean expectedResult, String testCase) {
    boolean result = Validations.HAS_VALID_PATH.test(path);
    assertEquals(expectedResult, result);
  }

  @ParameterizedTest(name = "{3}")
  @MethodSource("providePermissionTestCases")
  void testPathPermissions(boolean readable, boolean writable, boolean expectedResult, String testCase) {
    File file = tempDir.resolve("test.txt").toFile();
    try {
      file.createNewFile();
      file.setReadable(readable);
      file.setWritable(writable);

      boolean result = Validations.HAS_VALID_PERMISSIONS.test(file.toPath());
      assertEquals(expectedResult, result);
    } catch (IOException e) {
      fail("Failed to create test file", e);
    }
  }

  private static Stream<Arguments> providePathTestCases() {
    return Stream.of(
        Arguments.of(null, false, "Null path"),
        Arguments.of(validPath, true, "invalid path")
    );
  }

  private static Stream<Arguments> provideValidPathTestCases() {
    return Stream.of(
        Arguments.of(invalidPath, false, "Invalid path"),
        Arguments.of(validPath, true, "Valid path")
    );
  }

  private static Stream<Arguments> providePermissionTestCases() {
    return Stream.of(
        Arguments.of(true, true, true, "Both read and write permissions"),
        Arguments.of(true, false, false, "Only read permission"),
        Arguments.of(false, true, false, "Only write permission"),
        Arguments.of(false, false, false, "No permissions")
    );
  }
}
package com.roomelephant.elephub.adapters.docker.connect.path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.roomelephant.elephub.validations.ValidationRule;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DockerPathValidationTest {

  @TempDir
  Path tempDir;
  @Mock
  ValidationRule<Path> mockRule1;
  @Mock
  ValidationRule<Path> mockRule2;

  DockerPathValidation dockerPathValidation;


  @BeforeEach
  void setUp() {
    List<ValidationRule<Path>> rules = Arrays.asList(mockRule1, mockRule2);
    dockerPathValidation = new DockerPathValidation(rules);
  }

  @Test
  void shouldReturnTrueWhenAllValidationsPass() {
    Path path = tempDir.resolve("test.txt");
    when(mockRule1.validator()).thenReturn(p -> true);
    when(mockRule2.validator()).thenReturn(p -> true);

    boolean result = dockerPathValidation.validate(path);

    assertTrue(result);
    verify(mockRule1).validator();
    verify(mockRule2).validator();
  }

  @Test
  void shouldReturnFalseWhenFirstValidationFails() {
    Path path = tempDir.resolve("test.txt");
    when(mockRule1.validator()).thenReturn(p -> false);
    when(mockRule1.errorMessage()).thenReturn("First validation failed");

    boolean result = dockerPathValidation.validate(path);

    assertFalse(result);
    verify(mockRule1).validator();
    verify(mockRule1).errorMessage();
    verify(mockRule2, never()).validator();
  }

  @Test
  void shouldReturnFalseWhenSecondValidationFails() {
    Path path = tempDir.resolve("test.txt");
    when(mockRule1.validator()).thenReturn(p -> true);
    when(mockRule2.validator()).thenReturn(p -> false);
    when(mockRule2.errorMessage()).thenReturn("Second validation failed");

    boolean result = dockerPathValidation.validate(path);

    assertFalse(result);
    verify(mockRule1).validator();
    verify(mockRule2).validator();
    verify(mockRule2).errorMessage();
  }
} 
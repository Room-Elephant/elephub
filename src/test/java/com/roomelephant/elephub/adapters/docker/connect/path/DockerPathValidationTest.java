package com.roomelephant.elephub.adapters.docker.connect.path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.roomelephant.elephub.validations.ValidationResult;
import com.roomelephant.elephub.validations.internal.SimpleValidation;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DockerPathValidationTest {
  public static final Path PATH = Path.of("");
  @Mock
  SimpleValidation<Path> pathValidation;

  DockerPathValidation victim;


  @BeforeEach
  void setUp() {
    victim = new DockerPathValidation(pathValidation);
  }

  @Test
  void shouldReturnTrueWhenValidationsPass() {
    ValidationResult validationResult = new ValidationResult(List.of());
    when(pathValidation.validate(any())).thenReturn(validationResult);

    boolean result = victim.validate(PATH);

    assertTrue(result);
  }

  @Test
  void shouldReturnFalseWhenValidationFails() {
    ValidationResult validationResult = new ValidationResult(List.of("Error"));
    when(pathValidation.validate(any())).thenReturn(validationResult);

    boolean result = victim.validate(PATH);

    assertFalse(result);
  }
} 
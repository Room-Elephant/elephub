package com.roomelephant.elephub.validations.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.roomelephant.elephub.validations.ValidationResult;
import com.roomelephant.elephub.validations.ValidationRule;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleValidationTest {

  @Mock
  private ValidationRule<String> rule1;
  @Mock
  private ValidationRule<String> rule2;

  private SimpleValidation<String> victim;

  @BeforeEach
  void setUp() {
    victim = SimpleValidation.<String>builder()
        .rules(Arrays.asList(rule1, rule2))
        .build();
  }

  @Test
  void shouldBuildWithList() {
    List<ValidationRule<String>> result = victim.getRules();

    assertEquals(2, result.size());
    assertEquals(rule1, result.get(0));
    assertEquals(rule2, result.get(1));
  }

  @Test
  void shouldBuildWithElementAtTime() {
    SimpleValidation<String> victim = SimpleValidation.<String>builder()
        .rule(rule1)
        .rule(rule2)
        .build();

    List<ValidationRule<String>> result = victim.getRules();

    assertEquals(2, result.size());
    assertEquals(rule1, result.get(0));
    assertEquals(rule2, result.get(1));
  }

  @Test
  void shouldReturnUnmodifiableList() {
    List<ValidationRule<String>> result = victim.getRules();

    assertEquals(2, result.size());
    assertTrue(result.contains(rule1));
    assertTrue(result.contains(rule2));
    assertThrows(UnsupportedOperationException.class, () -> result.add(rule1));
  }

  @Test
  void shouldReturnValidWhenValidationsPassed() {
    when(rule1.validator()).thenReturn(p -> true);
    when(rule2.validator()).thenReturn(p -> true);

    ValidationResult result = victim.validate("test");

    assertTrue(result.isValid());
    assertTrue(result.errors().isEmpty());
  }

  @Test
  void shouldReturnInvalidWhenFirstValidationFails() {
    when(rule1.validator()).thenReturn(p -> false);
    when(rule1.errorMessage()).thenReturn("Error");

    ValidationResult result = victim.validate(null);

    verify(rule1).validator();
    verify(rule2, never()).validator();
    assertFalse(result.isValid());
    assertEquals(1, result.errors().size());
    assertTrue(result.errors().contains("Error"));
  }

  @Test
  void shouldReturnInvalidWhenSecondValidationFails() {
    when(rule1.validator()).thenReturn(p -> true);
    when(rule2.validator()).thenReturn(p -> false);
    when(rule2.errorMessage()).thenReturn("Error");

    ValidationResult result = victim.validate("");

    assertFalse(result.isValid());
    assertEquals(1, result.errors().size());
    assertTrue(result.errors().contains("Error"));
  }

  @Test
  void shouldHandleEmptyRulesList() {
    SimpleValidation<String> victim = SimpleValidation.<String>builder()
        .build();

    List<ValidationRule<String>> result = victim.getRules();
    assertTrue(result.isEmpty());
  }
} 
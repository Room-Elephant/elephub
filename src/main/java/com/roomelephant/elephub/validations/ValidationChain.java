package com.roomelephant.elephub.validations;

import java.util.ArrayList;
import java.util.List;

/**
 * A chainable interface for applying multiple validation rules on a given input.
 *
 * <p>The {@code ValidationChain} allows adding custom validation rules that check
 * whether a given value meets certain conditions. Each rule consists of a predicate
 * and an associated error message if validation fails.
 * </p>
 *
 * <p>The validation process is executed using the {@link #validate(Object)} method,
 * which returns a {@link ValidationResult} containing the validation outcome.
 * </p>
 *
 * @param <T> The type of the value being validated.
 */
public interface ValidationChain<T> {

  /**
   * Retrieves the list of validation rules in the validation chain.
   *
   * <p>This method returns the list of all rules added to the chain. Each rule consists of
   * a predicate and an error message, and is evaluated during the validation process.
   * </p>
   *
   * @return A list of {@link ValidationRule} objects representing the validation rules in the chain.
   */
  List<ValidationRule<T>> getRules();

  /**
   * Executes the validation chain.
   *
   * @param value The value to be validated.
   * @return A ValidationResult containing errors, if any.
   */
  default ValidationResult validate(T value) {
    List<String> errors = new ArrayList<>();
    for (ValidationRule<T> rule : getRules()) {
      if (!rule.validator().test(value)) {
        errors.add(rule.errorMessage());
        break;
      }
    }
    return new ValidationResult(errors);
  }
}
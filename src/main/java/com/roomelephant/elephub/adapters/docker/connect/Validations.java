package com.roomelephant.elephub.adapters.docker.connect;

/**
 * A generic interface for defining validation logic for various types of input.
 *
 * <p>Implementations of this interface should enforce specific validation rules
 * for objects of type {@code T}. If the validation fails, a {@link DockerConnectionException}
 * is thrown to indicate the reason for failure.
 * </p>
 *
 * @param <T> the type of input to be validated
 */
public interface Validations<T> {

  /**
   * Validates the given input against predefined rules.
   *
   * <p>If the input does not meet the required criteria, a {@link DockerConnectionException}
   * should be thrown to indicate the validation failure.
   * </p>
   *
   * @param input the object to be validated
   * @throws DockerConnectionException if the validation fails due to invalid input
   */
  void validate(T input) throws DockerConnectionException;
}
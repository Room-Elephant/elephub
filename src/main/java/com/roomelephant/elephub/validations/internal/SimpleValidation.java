package com.roomelephant.elephub.validations.internal;

import com.roomelephant.elephub.validations.ValidationChain;
import com.roomelephant.elephub.validations.ValidationRule;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

/**
 * A concrete implementation of the ValidationChain for simple validations.
 */
@Builder
public class SimpleValidation<T> implements ValidationChain<T> {

  @Singular
  private final List<ValidationRule<T>> rules;

  @Override
  public List<ValidationRule<T>> getRules() {
    return Collections.unmodifiableList(rules);
  }
}

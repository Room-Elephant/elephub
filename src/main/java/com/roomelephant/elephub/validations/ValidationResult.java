package com.roomelephant.elephub.validations;

import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import java.util.List;

@ExcludeFromJacocoGeneratedReport
public record ValidationResult(List<String> errors) {

  public boolean isValid() {
    return errors.isEmpty();
  }
}
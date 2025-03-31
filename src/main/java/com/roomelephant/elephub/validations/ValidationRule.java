package com.roomelephant.elephub.validations;

import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import java.util.function.Predicate;

@ExcludeFromJacocoGeneratedReport
public record ValidationRule<T>(Predicate<T> validator, String errorMessage) {
}
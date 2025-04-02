package com.roomelephant.elephub.adapters.docker.connect;

import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import java.util.function.Predicate;

@ExcludeFromJacocoGeneratedReport
record ValidationRule<T>(Predicate<T> validator, String errorMessage) {
}
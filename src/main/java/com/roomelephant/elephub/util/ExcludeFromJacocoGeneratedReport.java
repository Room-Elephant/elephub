package com.roomelephant.elephub.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the annotated class should be excluded from JaCoCo code coverage reports.
 *
 * <p>This annotation is useful for excluding generated code, utility classes, or other
 * non-testable components from coverage analysis.
 * </p>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {@code
 * @ExcludeFromJacocoGeneratedReport
 * public class MyExcludedClass {
 *     // This class will not be included in JaCoCo coverage reports
 * }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.CLASS) // Ensures it is available at compile time but ignored at runtime
@Target({ElementType.TYPE}) // Can only be applied to classes
public @interface ExcludeFromJacocoGeneratedReport {
}
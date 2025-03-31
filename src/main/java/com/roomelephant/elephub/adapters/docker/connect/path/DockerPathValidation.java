package com.roomelephant.elephub.adapters.docker.connect.path;

import com.roomelephant.elephub.validations.ValidationResult;
import com.roomelephant.elephub.validations.ValidationRule;
import com.roomelephant.elephub.validations.internal.SimpleValidation;
import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerPathValidation {
  private final SimpleValidation<Path> pathValidation;

  public DockerPathValidation(List<ValidationRule<Path>> rules) {
    pathValidation = SimpleValidation.<Path>builder()
        .rules(rules)
        .build();
  }

  public boolean validate(Path path) {
    ValidationResult result = pathValidation.validate(path);

    if (!result.isValid()) {
      log.error("operation='main', message='{}' socket='{}', user='{}', permissions='{can_read: {}, can_write: {}}'",
          result.errors().getFirst(), path.toAbsolutePath(), System.getProperty("user.name"),
          path.toFile().canRead(), path.toFile().canWrite());

      return false;
    }

    return true;
  }
}

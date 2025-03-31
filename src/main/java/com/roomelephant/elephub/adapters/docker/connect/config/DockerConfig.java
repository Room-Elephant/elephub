package com.roomelephant.elephub.adapters.docker.connect.config;

import static com.roomelephant.elephub.adapters.docker.connect.path.Validations.HAS_PATH;
import static com.roomelephant.elephub.adapters.docker.connect.path.Validations.HAS_VALID_PATH;
import static com.roomelephant.elephub.adapters.docker.connect.path.Validations.HAS_VALID_PERMISSIONS;

import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import com.roomelephant.elephub.validations.ValidationRule;
import java.nio.file.Path;
import java.util.List;

@ExcludeFromJacocoGeneratedReport
public class DockerConfig {
  public List<ValidationRule<Path>> pathValidationsRules() {
    return List.of(
        new ValidationRule<>(HAS_PATH, "Invalid path"),
        new ValidationRule<>(HAS_VALID_PATH, "Docker socket not found"),
        new ValidationRule<>(HAS_VALID_PERMISSIONS, "Insufficient permissions to access Docker socket")
    );
  }

  public String dockerPath() {
    return "/var/run/docker.sock";
  }
}

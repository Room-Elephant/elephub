package com.roomelephant.elephub.adapters.docker.connect;

import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class DockerConnectionException extends RuntimeException {
  public DockerConnectionException(String message, Exception e) {
    super(message, e);
  }

  public DockerConnectionException(String message) {
    super(message);
  }

  public DockerConnectionException(String path, String message) {
    super(message + ": '" + path + "'");
  }
}

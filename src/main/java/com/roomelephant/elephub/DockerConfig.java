package com.roomelephant.elephub;

import com.roomelephant.elephub.support.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class DockerConfig {

  public String dockerPath() {
    return "/var/run/docker.sock";
  }
}

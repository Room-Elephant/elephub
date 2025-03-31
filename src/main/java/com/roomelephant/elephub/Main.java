package com.roomelephant.elephub;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.adapters.docker.connect.Docker;
import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ExcludeFromJacocoGeneratedReport
public class Main {

  public static void main(String[] args) {
    log.info("operation='main', message='Elephub Application started'");

    Docker docker = Docker.getInstance();
    DockerClient dockerClient = docker.getDockerClient();

    dockerClient.listContainersCmd().exec().forEach(container -> {
      System.out.println("Container: " + container.getImage());
      System.out.println("\t" + container.getLabels());
    });

  }


}

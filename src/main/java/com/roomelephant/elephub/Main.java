package com.roomelephant.elephub;

import com.roomelephant.elephub.core.container.DockerCmd;
import com.roomelephant.elephub.core.container.DockerCmdClient;
import com.roomelephant.elephub.external.docker.DockerFacade;
import com.roomelephant.elephub.support.ExcludeFromJacocoGeneratedReport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExcludeFromJacocoGeneratedReport
public class Main {

  public static void main(String[] args) {

    DockerConfig dockerConfig = new DockerConfig();
    DockerFacade dockerApi = new DockerFacade();

    ((DockerCmdClient) dockerApi).initiateDockerClient(dockerConfig.dockerPath());

    ((DockerCmd) dockerApi).getContainers().forEach(container ->
        log.info("Container: {}", container)
    );
  }


}

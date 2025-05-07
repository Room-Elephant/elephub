package com.roomelephant.elephub.external.docker;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.core.container.DockerContainerWrapper;
import com.roomelephant.elephub.core.container.DockerCmd;
import com.roomelephant.elephub.core.container.DockerCmdClient;
import com.roomelephant.elephub.external.docker.command.DockerCommand;
import com.roomelephant.elephub.external.docker.connect.DockerClientFactory;
import com.roomelephant.elephub.external.docker.connect.PostValidations;
import com.roomelephant.elephub.external.docker.connect.PreValidations;
import com.roomelephant.elephub.external.docker.connect.Validations;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerFacade implements DockerCmd, DockerCmdClient {
  private DockerCommand command;

  @Override
  public void initiateDockerClient(String dockerPath) {
    Validations<String> preValidations = new PreValidations();
    Validations<DockerClient> postValidations = new PostValidations();
    DockerClientFactory factory = new DockerClientFactory(preValidations, postValidations);

    DockerClient dockerClient = factory.getDockerClient(dockerPath);
    command = new DockerCommand(dockerClient);
  }

  public List<DockerContainerWrapper> getContainers() {
    return command.getContainers();
  }
}

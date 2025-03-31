package com.roomelephant.elephub;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.adapters.docker.connect.DockerClientFactory;
import com.roomelephant.elephub.adapters.docker.connect.config.DockerConfig;
import com.roomelephant.elephub.adapters.docker.connect.exceptions.DockerExceptionConnection;
import com.roomelephant.elephub.adapters.docker.connect.path.DockerPathValidation;
import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
import com.roomelephant.elephub.validations.internal.SimpleValidation;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ExcludeFromJacocoGeneratedReport
public class Main {

  public static void main(String[] args) {
    log.info("operation='main', message='Elephub Application started'");

    DockerConfig dockerConfig = new DockerConfig();
    SimpleValidation<Path> pathValidationChain = SimpleValidation.<Path>builder()
        .rules(dockerConfig.pathValidationsRules())
        .build();
    DockerPathValidation pathValidation = new DockerPathValidation(pathValidationChain);
    DockerClientFactory factory = new DockerClientFactory(pathValidation);

    DockerClient dockerClient = null;
    try {
      dockerClient = factory.getDockerClient(dockerConfig.dockerPath());
    } catch (DockerExceptionConnection e) {
      // TODO: improve exception handling
      throw new RuntimeException(new DockerExceptionConnection());
    }

    dockerClient.listContainersCmd().exec().forEach(container -> {
      log.info("Container: {}, labels: {}", container.getImage(), container.getLabels());
    });

  }


}

package com.roomelephant.elephub;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.external.docker.connect.DockerClientFactory;
import com.roomelephant.elephub.external.docker.connect.DockerConnectionException;
import com.roomelephant.elephub.external.docker.connect.PostValidations;
import com.roomelephant.elephub.external.docker.connect.PreValidations;
import com.roomelephant.elephub.external.docker.connect.Validations;
import com.roomelephant.elephub.external.docker.command.DockerCmd;
import com.roomelephant.elephub.support.ExcludeFromJacocoGeneratedReport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExcludeFromJacocoGeneratedReport
public class Main {

  public static void main(String[] args) {
    log.info("operation='main', message='Elephub Application started'");

    DockerConfig dockerConfig = new DockerConfig();

    Validations<String> preValidations = new PreValidations();
    Validations<DockerClient> postValidations = new PostValidations();
    DockerClientFactory factory = new DockerClientFactory(preValidations, postValidations);

    DockerClient dockerClient = null;
    try {
      dockerClient = factory.getDockerClient(dockerConfig.dockerPath());
    } catch (DockerConnectionException e) {
      log.error("operation='main', message='Elephub Application stopped, verify your docker configurations'");
      System.exit(1);
    } catch (Exception e) {
      log.error("operation='main', message='Elephub Application stopped, something went wrong'");
      System.exit(1);
    }

    /*EnrichmentChain<com.github.dockerjava.api.model.Container, Container.ContainerBuilder> chain
        = new EnrichmentChain<>();
    chain.addEnricher(new InnerEnricher())
        .addEnricher(new BasicInformationEnricher());*/

    DockerCmd dockerCmd = new DockerCmd(dockerClient);


    dockerCmd.getContainers().forEach(container ->
        log.info("Container: {}", container)
    );
  }


}

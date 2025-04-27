package com.roomelephant.elephub;

import com.github.dockerjava.api.DockerClient;
import com.roomelephant.elephub.adapters.docker.connect.DockerClientFactory;
import com.roomelephant.elephub.adapters.docker.connect.DockerConnectionException;
import com.roomelephant.elephub.adapters.docker.connect.PostValidations;
import com.roomelephant.elephub.adapters.docker.connect.PreValidations;
import com.roomelephant.elephub.adapters.docker.connect.Validations;
import com.roomelephant.elephub.adapters.docker.fetch.ContainerCmd;
import com.roomelephant.elephub.adapters.docker.fetch.mapper.EnrichmentChain;
import com.roomelephant.elephub.adapters.docker.fetch.mapper.enricher.BasicInformationEnricher;
import com.roomelephant.elephub.adapters.docker.fetch.mapper.enricher.InnerEnricher;
import com.roomelephant.elephub.container.Container;
import com.roomelephant.elephub.util.ExcludeFromJacocoGeneratedReport;
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

    EnrichmentChain<com.github.dockerjava.api.model.Container, Container.ContainerBuilder> chain
        = new EnrichmentChain<>();
    chain.addEnricher(new InnerEnricher())
        .addEnricher(new BasicInformationEnricher());

    ContainerCmd containerCmd = new ContainerCmd(dockerClient, chain);


    containerCmd.fetch().forEach(container ->
        log.info("Container: {}", container)
    );
  }


}

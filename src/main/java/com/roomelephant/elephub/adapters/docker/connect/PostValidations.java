package com.roomelephant.elephub.adapters.docker.connect;

import com.github.dockerjava.api.DockerClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostValidations implements Validations<DockerClient> {

  @Override
  public void validate(DockerClient client) throws DockerConnectionException {
    try {
      log.debug("operation='validate', message='Attempting to connect to Docker daemon...'");
      client.pingCmd().exec();
    } catch (Exception e) {
      log.error("operation='validate', message='Failed to connect to Docker daemon: {}'",
          e.getMessage(), e);
      throw new DockerConnectionException(e.getMessage());
    }
  }
}

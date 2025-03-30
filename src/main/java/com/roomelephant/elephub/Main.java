package com.roomelephant.elephub;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static final String DOCKER_SOCKET = "/var/run/docker.sock";

    public static void main(String[] args) {
        log.info("operation='main', message='Elephub Application started'");

        Path socketPath = Path.of(DOCKER_SOCKET);
        if (hasInvalidPath(socketPath)) {
            log.error("operation='main', message='Docker socket not found at  {}'",
                    DOCKER_SOCKET);
            System.exit(1);
        }

        if (hasInvalidPermissions(socketPath)) {
            log.error("operation='main', message='Insufficient permissions to access Docker socket at {}', user='{}', permissions='{}'",
                    DOCKER_SOCKET, System.getProperty("user.name"), socketPath.toFile().getAbsolutePath());
            System.exit(1);
        }

        DockerClient dockerClient = getDockerClient();

        try {
            log.debug("operation='main', message='Attempting to connect to Docker daemon...'");
            dockerClient.pingCmd().exec();
        } catch (Exception e) {
            log.error("operation='main', message='Failed to connect to Docker daemon: {}'",
                    e.getMessage(), e);
            System.exit(1);
        }

        dockerClient.listContainersCmd().exec().forEach(container ->
                System.out.println("Container: " + container.getImage()));

    }

    private static DockerClient getDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix://" + DOCKER_SOCKET)
                .withDockerTlsVerify(false)
                .build();

        DockerHttpClient httpClient = new OkDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        return dockerClient;
    }

    private static boolean hasInvalidPath(Path socketPath) {
        return !socketPath.toFile().exists();
    }

    private static boolean hasInvalidPermissions(Path socketPath) {
        return !socketPath.toFile().canRead() || !socketPath.toFile().canWrite();
    }
}

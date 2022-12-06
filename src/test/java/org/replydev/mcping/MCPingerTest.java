package org.replydev.mcping;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.replydev.mcping.model.ServerResponse;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
class MCPingerTest {

    private static String minecraftServerHost;
    private static int minecraftServerPort;

    @Container
    public static GenericContainer<?> minecraftServerContainer = new GenericContainer<>(DockerImageName.parse("itzg/minecraft-server:latest"))
            .withExposedPorts(25565)
            .withEnv("EULA","true");


    @BeforeAll
    static void beforeAll(){
        minecraftServerContainer.start();
        minecraftServerHost = minecraftServerContainer.getHost();
        minecraftServerPort = minecraftServerContainer.getFirstMappedPort();
    }

    @Test
    void fetchData() throws IOException {
        PingOptions pingOptions = PingOptions.builder()
                .hostname(minecraftServerHost)
                .port(minecraftServerPort)
                .timeout(1000)
                .build();
        MCPinger mcPinger = MCPinger.builder().pingOptions(pingOptions).build();
        ServerResponse serverResponse = mcPinger.fetchData();
        assertNotNull(serverResponse);
    }
}
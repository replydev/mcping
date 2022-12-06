package org.replydev.mcping;

import lombok.Builder;
import lombok.Value;

import java.net.InetSocketAddress;

@Value
@Builder
public class PingOptions {
    String hostname;
    int port;
    int timeout;
    int protocolVersion = -1;

    public InetSocketAddress getSocketAddress() {
        return new InetSocketAddress(hostname, port);
    }
}

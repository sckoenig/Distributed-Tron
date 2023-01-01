package vsp.trongame.middleware.clientstub;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public interface ISender {

    enum Protocol {
        TCP, UDP
    }

    void send(byte[] message, InetSocketAddress address, Protocol protocol);

}

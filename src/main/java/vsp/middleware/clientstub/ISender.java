package vsp.middleware.clientstub;

import java.net.InetSocketAddress;

public interface ISender {

    enum Protocol {
        TCP, UDP
    }

    void send(byte[] message, InetSocketAddress address, Protocol protocol);

}

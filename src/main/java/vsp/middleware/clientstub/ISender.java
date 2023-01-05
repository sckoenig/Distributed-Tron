package vsp.middleware.clientstub;

import java.net.InetSocketAddress;

public interface ISender {

    enum Protocol {
        TCP, UDP
    }

    /**
     * Opens a socket for the given address and sends a message.
     *
     * @param message which we want to send
     * @param address for the socket
     * @param protocol which socket we want to create
     */
    void send(byte[] message, InetSocketAddress address, Protocol protocol);

}

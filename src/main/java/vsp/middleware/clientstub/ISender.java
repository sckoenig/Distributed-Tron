package vsp.middleware.clientstub;

import java.net.InetSocketAddress;

public interface ISender {

    /**
     * Represents Protocol Types a sender can use.
     */
    enum Protocol {
        TCP, UDP
    }

    /**
     * Sends a message to a given address with the given protocol.
     * @param message message to send
     * @param address address to send to
     * @param protocol protocol that shall be used
     */
    void send(byte[] message, InetSocketAddress address, Protocol protocol);

}

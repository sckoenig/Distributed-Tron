package vsp.middleware.clientstub;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

/**
 * Enables message sending via UDP or TCP.
 */
public class Sender implements ISender {

    private final DatagramSocket udpSocket;

    public Sender() throws SocketException {
        this.udpSocket = new DatagramSocket();
    }

    @Override
    public void send(byte[] message, InetSocketAddress address, Protocol protocol) {
        try {

            if (protocol == Protocol.TCP) {
                try (Socket clientSocket = new Socket(address.getAddress(), address.getPort())) {
                    OutputStream out = clientSocket.getOutputStream();
                    out.write(message.length);
                    out.write(message);
                }
            }
            if (protocol == Protocol.UDP) {
                    DatagramPacket packet = new DatagramPacket(message, message.length, address);
                    udpSocket.send(packet);
            }

        } catch (IOException e){
            // catch and abort if the receiver is not available
        }
    }

}

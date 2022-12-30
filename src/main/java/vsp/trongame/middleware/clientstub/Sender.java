package vsp.trongame.middleware.clientstub;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class Sender implements ISender {

    private DatagramSocket udpSocket;

    public Sender() throws SocketException {
        this.udpSocket = new DatagramSocket();
    }

    @Override
    public void send(byte[] message, InetSocketAddress address, Protocol protocol) throws IOException {
        switch(protocol){
            case TCP -> {
                Socket clientSocket = new Socket(address.getAddress(), address.getPort());
                OutputStream out = clientSocket.getOutputStream();
                out.write(message.length);
                out.write(message);
            }
            case UDP -> {
                DatagramPacket packet = new DatagramPacket(message, message.length, address);
                udpSocket.send(packet);
            }
        }
    }
}

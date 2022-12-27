package vsp.trongame.middleware.serverstub;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Receiver {

    private static final int PACKAGE_SIZE = 1400;
    private DatagramSocket udpSocket;
    private ServerSocket tcpSocket;

    private Unmarshaller unmarshaller;
    private final BlockingQueue<Socket> queue;

    public Receiver() throws IOException {
        queue = new LinkedBlockingQueue<>();
        unmarshaller = new Unmarshaller();
        udpSocket = new DatagramSocket();
        tcpSocket = new ServerSocket();

        int portMin = 1024;
        int portMax = 49152;
        int port;
        boolean success = false;
        InetAddress ipAddress = InetAddress.getLocalHost();

        while (!success){
           port = (int)(Math.random() * (portMax - portMin)) + portMin;

           try{
               SocketAddress address = new InetSocketAddress(ipAddress, port);
               tcpSocket.bind(address);
               udpSocket.bind(address);
               success = true;
            } catch (IOException e) {
               e.printStackTrace();
            }
        }

        startTcpSocket();
        startUdpSocket();

    }

    private void startTcpSocket(){

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    Socket clientSocket = queue.take();
                    byte[] message = clientSocket.getInputStream().readAllBytes();
                    unmarshaller.addToQueue(message);
                } catch (InterruptedException | IOException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    Socket clientSocket = tcpSocket.accept();
                    queue.add(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startUdpSocket(){
        new Thread(() -> {

            byte[] buffer = new byte[PACKAGE_SIZE];

            while (!Thread.currentThread().isInterrupted()){
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, PACKAGE_SIZE);
                    udpSocket.receive(packet);
                    unmarshaller.addToQueue(packet.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
}

package vsp.trongame.middleware.serverstub;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class Receiver {
    private static final int PACKAGE_SIZE = 1400;
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 49152;
    private final DatagramSocket udpSocket;
    private final ServerSocket tcpSocket;
    private final IUnmarshaller unmarshaller;
    private final BlockingQueue<Socket> tcpSocketQueue;
    private final Random rand;
    private final ExecutorService executorService;
    private final byte[] udpPacketBuffer;

    public Receiver(IUnmarshaller unmarshaller, ExecutorService executorService) throws IOException {
        this.unmarshaller = unmarshaller;
        this.executorService = executorService;
        this.tcpSocketQueue = new LinkedBlockingQueue<>();
        this.udpPacketBuffer = new byte[PACKAGE_SIZE];
        this.udpSocket = new DatagramSocket();
        this.tcpSocket = new ServerSocket();
        this.rand = new Random();

        createServerSockets();

    }

    private void createServerSockets() throws UnknownHostException {
        int port;
        boolean success = false;
        InetAddress ipAddress = InetAddress.getLocalHost();

        while (!success){
            port = rand.nextInt(MIN_PORT, MAX_PORT+1);
            try{
                SocketAddress address = new InetSocketAddress(ipAddress, port);
                tcpSocket.bind(address);
                udpSocket.bind(address);
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        startTcpReceiver();
        startUdpReceiver();
    }

    private void startTcpReceiver(){
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    Socket clientSocket = tcpSocket.accept();
                    tcpSocketQueue.add(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        startTcpSocketHandler();
    }

    private void startTcpSocketHandler(){
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try (Socket clientSocket = tcpSocketQueue.take()) {

                    byte[] message = clientSocket.getInputStream().readAllBytes();
                    unmarshaller.addToQueue(message);

                } catch (InterruptedException | IOException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private void startUdpReceiver(){
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    DatagramPacket packet = new DatagramPacket(udpPacketBuffer, PACKAGE_SIZE);
                    udpSocket.receive(packet);
                    unmarshaller.addToQueue(packet.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    
}

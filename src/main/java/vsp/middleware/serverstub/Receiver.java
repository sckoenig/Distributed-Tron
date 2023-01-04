package vsp.middleware.serverstub;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class Receiver {
    private static final int PACKAGE_SIZE = 1400;
    private static final int MIN_PORT = 5555;
    private static final int MAX_PORT = 49152;
    private DatagramSocket udpSocket;
    private ServerSocket tcpSocket;
    private final IUnmarshaller unmarshaller;
    private final BlockingQueue<Socket> tcpSocketQueue;
    private final Random rand;
    private final ExecutorService executorService;

    public Receiver(IUnmarshaller unmarshaller, ExecutorService executorService) {
        this.unmarshaller = unmarshaller;
        this.executorService = executorService;
        this.tcpSocketQueue = new LinkedBlockingQueue<>();
        this.rand = new Random();
    }

    public void start() {
        createReceiverSockets();
        startTcpReceiver();
        startUdpReceiver();
    }

    public void stop() {
        try {
            tcpSocket.close();
            udpSocket.close();
        } catch (IOException e) {
            // we want server socket to close here, but will throw exception if still blocked in accept().
        }
    }

    private void createReceiverSockets() {
        int port;
        boolean portAvailable = false;

        while (!portAvailable) {
            port = rand.nextInt(MIN_PORT, MAX_PORT + 1);

            try {
                this.udpSocket = new DatagramSocket(port);
                this.tcpSocket = new ServerSocket();
                tcpSocket.bind(new InetSocketAddress(port));

                portAvailable = true;
                unmarshaller.setPort(port);
            } catch (IOException e) {
                // catch and roll another port
            }
        }
    }

    private void startTcpReceiver() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    Socket clientSocket = tcpSocket.accept();
                    tcpSocketQueue.add(clientSocket);
                } catch (IOException e) {
                    // catch and abort on socket error
                }
            }
        });

        startTcpSocketHandler();
    }

    private void startTcpSocketHandler() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {

                try (Socket clientSocket = tcpSocketQueue.take()) {
                    int messageLength = clientSocket.getInputStream().read();
                    byte[] message = clientSocket.getInputStream().readNBytes(messageLength);
                    unmarshaller.addToQueue(message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    // catch and abort on socket error
                }
            }
        });
    }


    private void startUdpReceiver() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    byte[] udpPacketBuffer = new byte[PACKAGE_SIZE];
                    DatagramPacket packet = new DatagramPacket(udpPacketBuffer, PACKAGE_SIZE);
                    udpSocket.receive(packet);
                    unmarshaller.addToQueue(packet.getData());
                } catch (IOException e) {
                    // we want server socket to close here, but will throw exception if still blocked in accept().
                }
            }
        });
    }

}

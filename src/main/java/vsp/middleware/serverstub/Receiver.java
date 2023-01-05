package vsp.middleware.serverstub;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Enables message receiving via UDP and TCP.
 */
public class Receiver implements IReceiver{
    private DatagramSocket udpSocket;
    private ServerSocket tcpSocket;
    private final IUnmarshaller unmarshaller;
    private final BlockingQueue<Socket> tcpSocketQueue;
    private final Random rand; // roll random port
    private final ExecutorService executorService;

    public Receiver(IUnmarshaller unmarshaller, ExecutorService executorService) {
        this.unmarshaller = unmarshaller;
        this.executorService = executorService;
        this.tcpSocketQueue = new LinkedBlockingQueue<>();
        this.rand = new Random();
    }

    @Override
    public void start() {
        createReceiverSockets();
        startTcpReceiver();
        startUdpReceiver();
    }

    @Override
    public void stop() {
        try {
            tcpSocket.close();
            udpSocket.close();
        } catch (IOException e) {
            // we want server socket to close here, but will throw exception if still blocked in accept().
        }
    }

    /**
     * Creates listening Sockets with random port.
     */
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

    /**
     * Starts a thread for the TCP-ServerSocket.
     */
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

    /**
     * Starts a thread that handles incoming Sockets from TCP-ServerSocket.
     */
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

    /**
     * Starts a thread for the UDP-DatagramSocket.
     */
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

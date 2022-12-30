package vsp.trongame.middleware.serverstub;

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
    private final byte[] udpPacketBuffer;

    public Receiver(IUnmarshaller unmarshaller, ExecutorService executorService) throws IOException {
        this.unmarshaller = unmarshaller;
        this.executorService = executorService;
        this.tcpSocketQueue = new LinkedBlockingQueue<>();
        this.udpPacketBuffer = new byte[PACKAGE_SIZE];
        this.rand = new Random();

        createServerSockets();

    }

    private void createServerSockets() throws UnknownHostException {
        int port;
        boolean success = false;
        //InetAddress ipAddress = InetAddress.getLocalHost();
        InetAddress ipAddress = InetAddress.getByName("169.254.171.162");
        System.out.println(ipAddress);

        while (!success){
            port = rand.nextInt(MIN_PORT, MAX_PORT+1);
            try{

                this.udpSocket = new DatagramSocket( port, ipAddress);
                this.tcpSocket = new ServerSocket( );
                tcpSocket.bind(new InetSocketAddress(ipAddress, port));
                tcpSocket.setReuseAddress(true);
                udpSocket.setReuseAddress(true);

                success = true;
                unmarshaller.setAddress(new InetSocketAddress(ipAddress, port));
            } catch (IOException e) {
                System.out.println("CATCH");
            }
        }

        startTcpReceiver();
        startUdpReceiver();
    }

    private void startTcpReceiver(){
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    System.out.println("TCP RECEIVER ALIVE");
                    Socket clientSocket = tcpSocket.accept();
                    tcpSocketQueue.add(clientSocket);
                } catch (IOException e) {
                    Thread.currentThread().interrupt();
                    //logging
                }
            }
        });

        startTcpSocketHandler();
    }

    private void startTcpSocketHandler(){
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()){
                System.out.println("TCP SOCKET HANDLER ALIVE");
                try (Socket clientSocket = tcpSocketQueue.take()) {
                    int length = clientSocket.getInputStream().read();
                    byte[] message = clientSocket.getInputStream().readNBytes(length);
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
                    System.out.println("UDP RECEIVER ALIVE");
                    DatagramPacket packet = new DatagramPacket(udpPacketBuffer, PACKAGE_SIZE);
                    udpSocket.receive(packet);
                    unmarshaller.addToQueue(packet.getData());
                } catch (IOException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

    }

    public void shutDown(){
        try {
            tcpSocket.close();
            udpSocket.close();
        } catch (IOException e){
            //logging irgendwann
        }
    }
    
}

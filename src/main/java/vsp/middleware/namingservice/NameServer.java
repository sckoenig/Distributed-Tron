package vsp.middleware.namingservice;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Enables Registration and Lookup for Services.
 */
public class NameServer implements INamingService {

    private final Map<Integer, Map<String, String>> services;
    private final InetSocketAddress address;
    private final Gson gson;
    private final ExecutorService executorService;
    private ServerSocket tcpServerSocket;

    public NameServer(InetSocketAddress address) {
        this.address = address;
        this.gson = new Gson();
        this.services = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public String lookupService(String remoteID, int serviceId) {
        if (remoteID.equals(NamingServiceMessage.NO_REMOTE_ID)) {
            remoteID = services.get(serviceId).entrySet().iterator().next().getKey(); //LinkedHashMap, returns the first inserted key.
        }
        return services.get(serviceId).get(remoteID);
    }

    @Override
    public void registerService(String remoteID, int serviceID, String address) {
        services.computeIfAbsent(serviceID, v -> new LinkedHashMap<>()).put(remoteID, address);
    }

    @Override
    public void unregisterService(String remoteID) {
        for (Map.Entry<Integer, Map<String, String>> entry : services.entrySet()) {
            entry.getValue().remove(remoteID);
        }
    }

    /**
     * Starts this name server.
     */
    public void start() {
        executorService.execute(this::runServerSocket);
    }

    /**
     * Starts the ServerSocket listening for incoming messages.
     */
    private void runServerSocket() {
        try {
            this.tcpServerSocket = new ServerSocket(address.getPort());

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSocket = this.tcpServerSocket.accept();

                int messageLength = clientSocket.getInputStream().read();//needs to know how much to read from stream
                byte[] byteMessage = clientSocket.getInputStream().readNBytes(messageLength);

                String jsonMessage = new String(byteMessage, StandardCharsets.UTF_8);
                NamingServiceMessage message = gson.fromJson(jsonMessage, NamingServiceMessage.class);

                processMessage(message, clientSocket);
                clientSocket.close();
            }

        } catch (IOException e) {
            // on close() server socket will throw exception if still blocked in accept()
        }
    }

    /**
     * Processes a received message.
     * @param message the message to process
     * @param clientSocket the socket the message was received form, incase a response is expected.
     * @throws IOException on socket error
     */
    private void processMessage(NamingServiceMessage message, Socket clientSocket) throws IOException {

        if (message.messageType() == NamingServiceMessage.LOOKUP) {
            String requestedService = lookupService(message.remoteId(), message.serviceId());
            NamingServiceMessage response = new NamingServiceMessage(NamingServiceMessage.RESPONSE,
                    NamingServiceMessage.NO_SERVICE, NamingServiceMessage.NO_REMOTE_ID, requestedService);
            byte[] responseMessage = gson.toJson(response).getBytes(StandardCharsets.UTF_8);

            clientSocket.getOutputStream().write(responseMessage.length);
            clientSocket.getOutputStream().write(responseMessage);
        }
        if (message.messageType() == NamingServiceMessage.REGISTER) {
            registerService(message.remoteId(), message.serviceId(), message.address());
        }
        if (message.messageType() == NamingServiceMessage.UNREGISTER) {
            unregisterService(message.remoteId());
        }
    }

    /**
     * Stops this name server.
     */
    public void stop() {
        try {
            tcpServerSocket.close();
            executorService.shutdownNow();
        } catch (IOException e) {
            // we want server socket to close here, but will throw exception if still blocked in accept().
        }
    }
}

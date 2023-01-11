package vsp.middleware.namingservice;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * Represents a local name resolver for registration and lookup purposes.
 */
public class NameResolver implements INamingService {

    private static final int CACHE_CLEARANCE_INTERVAL = 15000;
    private static final int TIMEOUT = 1000;
    private final Map<Integer, Map<String, String>> cache;
    private final InetSocketAddress serverAddress;
    private final Gson gson;
    private final ExecutorService executorService;
    private final ReentrantLock cacheLock;

    public NameResolver(ExecutorService executorService, InetSocketAddress address) {
        this.serverAddress = address;
        this.executorService = executorService;
        this.cache = new HashMap<>();
        this.gson = new Gson();
        this.cacheLock = new ReentrantLock();

        startClearCache();
    }

    @Override
    public String lookupService(String remoteID, int serviceId) {
        String address = lookUpCache(remoteID, serviceId);

        if (address == null) {
            return sendRequest(NamingServiceMessage.LOOKUP, serviceId, remoteID, NamingServiceMessage.NO_ADDRESS, true);
        }
        return address;
    }

    @Override
    public void registerService(String remoteID, int serviceID, String address) {
        sendRequest(NamingServiceMessage.REGISTER, serviceID, remoteID, address, false);
    }

    @Override
    public void unregisterService(String remoteID) {
        sendRequest(NamingServiceMessage.UNREGISTER, NamingServiceMessage.NO_SERVICE, remoteID, NamingServiceMessage.NO_ADDRESS, false);
    }

    /**
     * Sends a message to the name server.
     * @param messageType the type of the message
     * @param serviceId the serviceId or default
     * @param remoteId the remoteId or default
     * @param address the address or default
     * @param awaitResponse true, if a response is necessary, false otherwise.
     * @return the response, if awaitResponse was true, empty String otherwise.
     */
    private String sendRequest(byte messageType, int serviceId, String remoteId, String address, boolean awaitResponse){

        NamingServiceMessage message = new NamingServiceMessage(messageType, serviceId, remoteId, address);
        byte[] byteMessage = gson.toJson(message).getBytes(StandardCharsets.UTF_8);
        String response = "";

        try (Socket clientSocket = new Socket()){
            clientSocket.connect(serverAddress, TIMEOUT);
            clientSocket.getOutputStream().write(byteMessage.length);
            clientSocket.getOutputStream().write(byteMessage);

            if (awaitResponse){

                int responseLength = clientSocket.getInputStream().read();
                byte[] byteResponse = clientSocket.getInputStream().readNBytes(responseLength);
                String jsonResponse = new String(byteResponse, StandardCharsets.UTF_8);
                NamingServiceMessage responseMessage = gson.fromJson(jsonResponse, NamingServiceMessage.class);

                //save in cache
                cache.computeIfAbsent(serviceId, v -> new HashMap<>()).put(remoteId, responseMessage.address());

                response = responseMessage.address();
            }

        } catch (IOException e) {
            // should use logger instead
            System.err.println("NameServer Error: " + e.getMessage());
        }
        return response;
    }

    /**
     * Clears the cache in regular intervals.
     */
    private void startClearCache() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // noinspection BusyWait: cache clearance interval
                    sleep(CACHE_CLEARANCE_INTERVAL);
                    cacheLock.lock();
                    cache.clear();
                    cacheLock.unlock();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    /**
     * Looks for service information in cache.
     * @param remoteId the service provider's remoteId
     * @param serviceId the service's id.
     * @return the service prodiver's address, if known, null otherwise.
     */
    private String lookUpCache(String remoteId, int serviceId){
        cacheLock.lock();
        Map<String, String> serviceProvider = cache.get(serviceId);
        cacheLock.unlock();

        if (serviceProvider != null) return serviceProvider.get(remoteId);
        else return null;
    }


}

package vsp.trongame.middleware.namingservice;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.sleep;

public class NameResolver implements INamingService {
    private static final int CACHE_CLEARANCE_INTERVAL = 15000;
    private static final int TIMEOUT = 3000;
    private final Map<Integer, Map<String, String>> cache;

    private final InetSocketAddress serverAddress;
    private final Gson gson;
    private final ExecutorService executorService;

    public NameResolver(ExecutorService executorService, InetSocketAddress address) {
        this.serverAddress = address;
        this.executorService = executorService;
        this.cache = new HashMap<>();
        this.gson = new Gson();

        startClearCache();
    }

    @Override
    public String lookupService(String remoteID, int serviceId) {
        String address = lookUpCache(remoteID, serviceId);

        if (address == null) {
            return sendRequest(LOOKUP, serviceId, remoteID, null, true);
        }
        return address;
    }

    @Override
    public void registerService(String remoteID, int serviceID, String address) {
        sendRequest(REGISTER, serviceID, remoteID, address, false);
    }

    @Override
    public void unregisterService(String remoteID) {
        sendRequest(UNREGISTER, NO_SERVICE, remoteID, null, false);
    }

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

                return responseMessage.address();
            }

        } catch (IOException e) {
            // should use logger instead
            System.err.println("NameServer Error: " + e.getMessage());
        }
        return response;
    }

    private void startClearCache() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // noinspection BusyWait: cache clearance interval
                    sleep(CACHE_CLEARANCE_INTERVAL);
                    cache.clear();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private String lookUpCache(String remoteId, int serviceId){
        Map<String, String> serviceProvider = cache.get(serviceId);

        if (serviceProvider != null) return serviceProvider.get(remoteId);
        else return null;
    }


}

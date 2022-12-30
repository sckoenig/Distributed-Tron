package vsp.trongame.middleware.namingservice;

import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.sleep;

public class NameResolver implements INamingService {
    private static final int CACHE_TIME = 15000;
    private Map<Integer, Map<String, String>> cache;
    private Socket clientSocket;

    private InetSocketAddress serverAddress;
    private final Gson gson;
    private final ExecutorService executorService;

    public NameResolver(InetSocketAddress serverAddress, ExecutorService executorService) {
        this.executorService = executorService;
        this.serverAddress = serverAddress;
        this.cache = new HashMap<>();
        this.gson = new Gson();
        System.out.println();

        startClearCache();
    }

    private void startClearCache() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // noinspection BusyWait: cache clearance interval
                    sleep(CACHE_TIME);
                    cache.clear();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    @Override
    public String lookupService(String remoteID, int serviceId) {
        Map<String, String> map = cache.get(serviceId);
        String address = null;
        if (map != null) address = map.get(remoteID);
        if (address == null) {
            try {
                clientSocket = new Socket(serverAddress.getAddress(), serverAddress.getPort());
                NamingServiceMessage message = new NamingServiceMessage(LOOKUP, serviceId, remoteID, null);
                byte[] byteMessage = gson.toJson(message).getBytes(StandardCharsets.UTF_8);
                clientSocket.getOutputStream().write(byteMessage.length);
                clientSocket.getOutputStream().write(byteMessage);
                int length = clientSocket.getInputStream().read();
                byte[] response = clientSocket.getInputStream().readNBytes(length);
                String jsonResponse = new String(response, StandardCharsets.UTF_8);
                NamingServiceMessage responseMessage = gson.fromJson(jsonResponse, NamingServiceMessage.class);

                cache.computeIfAbsent(serviceId, v -> new HashMap<>()).put(remoteID, responseMessage.address());
                address = responseMessage.address();

                return address;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return address;
    }

    @Override
    public void registerService(String remoteID, int serviceID, String address) {
        try {
            clientSocket = new Socket(serverAddress.getAddress(), serverAddress.getPort());
            NamingServiceMessage message = new NamingServiceMessage(REGISTER, serviceID, remoteID, address);
            String json = gson.toJson(message);
            byte[] byteMessage = json.getBytes(StandardCharsets.UTF_8);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.write(byteMessage.length);
            out.write(byteMessage);
        } catch (IOException e) {
            error();
        }
    }


    @Override
    public void unregisterService(String remoteID) {
        try {
            clientSocket = new Socket(serverAddress.getAddress(), serverAddress.getPort());
            NamingServiceMessage message = new NamingServiceMessage(UNREGISTER, -1, remoteID, null);
            byte[] byteMessage = gson.toJson(message).getBytes(StandardCharsets.UTF_8);
            clientSocket.getOutputStream().write(byteMessage);
        } catch (IOException e) {
            error();
        }
    }

    private void error() {
        System.out.println("SERVER NCIHT ERREICHBAR");
    }
}

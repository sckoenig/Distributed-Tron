package vsp.trongame.middleware.namingservice;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NameServer implements INamingService {

    private final Map<Integer, Map<String, InetSocketAddress>> services;
    private final ServerSocket tcpServerSocket;
    private final Gson gson;

    public NameServer(String address) throws IOException {
        String[] split = address.split(":");
        this.tcpServerSocket = new ServerSocket();
        tcpServerSocket.bind(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
        this.gson = new Gson();
        this.services = new HashMap<>();
    }

    public void start() throws IOException {
        startServerSocket();
    }

    private void startServerSocket() throws IOException {

        while (!Thread.currentThread().isInterrupted()) {
            Socket clientSocket = this.tcpServerSocket.accept();
            System.out.println("REQUEST");
            int length = clientSocket.getInputStream().read();
            byte[] byteMessage = clientSocket.getInputStream().readNBytes(length);
            System.out.println(byteMessage);
            String jsonMessage = new String(byteMessage, StandardCharsets.UTF_8);
            NamingServiceMessage message = gson.fromJson(jsonMessage, NamingServiceMessage.class);
            System.out.println("MESSAGE:" +jsonMessage);

            if (message.messageType() == LOOKUP) {
                System.out.println("NAME SERVER REQUEST LOOKUP");
                InetSocketAddress result = lookupService(message.remoteId(), message.serviceId());
                NamingServiceMessage response = new NamingServiceMessage(RESPONSE, -1, "", result);
                byte[] responseMessage = gson.toJson(response).getBytes(StandardCharsets.UTF_8);
                clientSocket.getOutputStream().write(responseMessage.length);
                clientSocket.getOutputStream().write(responseMessage);
            }
            if (message.messageType() == REGISTER) {
                System.out.println("NAME SERVER REQUEST REGISTER");
                registerService(message.remoteId(), message.serviceId(), message.address());
            }
            if (message.messageType() == UNREGISTER) {
                unregisterService(message.remoteId());
            }
        }
    }

    @Override
    public InetSocketAddress lookupService(String remoteID, int serviceId) {
        return services.get(serviceId).get(remoteID);
    }

    @Override
    public void registerService(String remoteID, int serviceID, InetSocketAddress address) {
        services.computeIfAbsent(serviceID, v -> new HashMap<>()).put(remoteID, address);
    }

    @Override
    public void unregisterService(String remoteID) {
        for (Map.Entry<Integer, Map<String, InetSocketAddress>> entry: services.entrySet()) {
            entry.getValue().remove(remoteID);
        }
    }

    public void shutDown(){
        try {
            tcpServerSocket.close();
        } catch (IOException e){
            //
        }
    }
}

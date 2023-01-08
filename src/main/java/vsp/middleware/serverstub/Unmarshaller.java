package vsp.middleware.serverstub;

import com.google.gson.Gson;
import vsp.middleware.IRegister;
import vsp.middleware.IRemoteObject;
import vsp.middleware.namingservice.INamingService;
import vsp.middleware.ServiceCall;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class Unmarshaller implements IUnmarshaller, IRegister {

    private final Map<Integer, IRemoteObject> remoteObjectRegister;
    private final BlockingQueue<byte[]> messageQueue;
    private final ExecutorService executorService;
    private final Gson gson;
    private final INamingService namingService;
    private final Receiver receiver;
    private InetSocketAddress serverStubAddress;
    private final String ip;

    public Unmarshaller(ExecutorService executorService, String ip, INamingService namingService){
        this.ip = ip;
        this.namingService = namingService;
        this.gson = new Gson();
        this.executorService = executorService;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.remoteObjectRegister = new HashMap<>();
        this.receiver = new Receiver(this, executorService);

        startServiceCallHandler(); //handles tasks in queue
        receiver.start();
    }

    private void startServiceCallHandler() {
        executorService.execute(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    byte[] message = messageQueue.take();
                    String json = new String(message, StandardCharsets.UTF_8).trim();
                    ServiceCall call = gson.fromJson(json, ServiceCall.class);

                    IRemoteObject remoteObject = remoteObjectRegister.get(call.serviceId());
                    if (remoteObject != null) {
                        remoteObject.call(call.serviceId(), call.intParameters(), call.stringParameters());
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void stop() {
        receiver.stop();
    }

    @Override
    public void addToQueue(byte[] message) {
        messageQueue.add(message);
    }

    @Override
    public void setPort(int port) {
        this.serverStubAddress = new InetSocketAddress(ip, port);
        System.out.println("UNMARSHALLER " +serverStubAddress);
    }

    @Override
    public void registerRemoteObject(int serviceID, String remoteId, IRemoteObject remoteObject) {
        remoteObjectRegister.put(serviceID, remoteObject);
        namingService.registerService(remoteId, serviceID, serverStubAddress.getAddress().getHostAddress()+":"+ serverStubAddress.getPort());
    }

}

package vsp.trongame.middleware.serverstub;

import com.google.gson.Gson;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;
import vsp.trongame.middleware.namingservice.INamingService;
import vsp.trongame.middleware.util.ServiceCall;

import java.io.IOException;
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
    private InetSocketAddress address;
    private Receiver receiver;

    public Unmarshaller(ExecutorService executorService, INamingService namingService){
        this.namingService = namingService;
        this.gson = new Gson();
        this.executorService = executorService;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.remoteObjectRegister = new HashMap<>();

        try {
            this.receiver = new Receiver(this, executorService);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startServiceCallHandler(); //handles tasks in queue
    }

    private void startServiceCallHandler() {
        executorService.execute(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    byte[] message = messageQueue.take();
                    String json = new String(message, StandardCharsets.UTF_8).strip().trim();
                    ServiceCall call = gson.fromJson(json, ServiceCall.class);
                    System.out.println("RECEIVED: "+json);

                    IRemoteObject remoteObject = remoteObjectRegister.get(call.serviceId());
                    if (remoteObject != null)
                        remoteObject.call(call.serviceId(), call.intParameters(), call.stringParameters());

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    @Override
    public void addToQueue(byte[] message) {
        messageQueue.add(message);
    }

    @Override
    public void setPort(int port) {
        try {
            this.address = new InetSocketAddress(InetAddress.getLocalHost(), port);
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    @Override
    public void registerRemoteObject(int serviceID, String remoteId, IRemoteObject remoteObject) {
        remoteObjectRegister.put(serviceID, remoteObject);
        namingService.registerService(remoteId, serviceID, address.getAddress().getHostAddress()+":"+address.getPort());
    }

    public void shutDown(){
        receiver.shutDown();
    }
}

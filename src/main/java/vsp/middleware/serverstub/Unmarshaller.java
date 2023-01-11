package vsp.middleware.serverstub;

import com.google.gson.Gson;
import vsp.middleware.IRegister;
import vsp.middleware.IRemoteObject;
import vsp.middleware.namingservice.INamingService;
import vsp.middleware.ServiceCall;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Enables Unmarshalling and Registration {@link IRemoteObject}.
 */
public class Unmarshaller implements IUnmarshaller, IRegister {

    private final Map<Integer, IRemoteObject> remoteObjectRegister;
    private final BlockingQueue<byte[]> messageQueue;
    private final ExecutorService executorService; // for queue
    private final Gson gson; // for unmarshalling
    private final INamingService namingService;
    private final Receiver receiver;
    private InetSocketAddress serverStubAddress;
    private String remoteId;
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

    /**
     * Starts a thread that handels messages in queue.
     */
    private void startServiceCallHandler() {
        executorService.execute(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {

                    byte[] message = messageQueue.take();
                    ServiceCall call = unmarshal(message);
                    callOnRemoteObject(call);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    /**
     * Calls a Service on a {@link IRemoteObject}. If the object to call is unknown, the call is ignored.
     * @param call Service call
     */
    private void callOnRemoteObject(ServiceCall call){
        IRemoteObject remoteObject = remoteObjectRegister.get(call.serviceId());
        if (remoteObject != null) {
            remoteObject.call(call.serviceId(), call.intParameters(), call.stringParameters());
        }
    }

    /**
     * Unmarshalls a message to {@link ServiceCall}.
     * @param message byte message
     * @return service call
     */
    private ServiceCall unmarshal(byte[] message){
        String json = new String(message, StandardCharsets.UTF_8).trim();
        return gson.fromJson(json, ServiceCall.class);
    }

    @Override
    public void stop() {
        receiver.stop();
        namingService.unregisterService(remoteId);
    }

    @Override
    public void addToQueue(byte[] message) {
        messageQueue.add(message);
    }

    @Override
    public void setPort(int port) {
        this.serverStubAddress = new InetSocketAddress(ip, port);
        System.out.println("SERVERSTUB: " + serverStubAddress);
    }

    @Override
    public void registerRemoteObject(int serviceID, String remoteId, IRemoteObject remoteObject) {
        this.remoteId = remoteId;
        remoteObjectRegister.put(serviceID, remoteObject);
        namingService.registerService(remoteId, serviceID, serverStubAddress.getAddress().getHostAddress()+":"+ serverStubAddress.getPort());
    }

}

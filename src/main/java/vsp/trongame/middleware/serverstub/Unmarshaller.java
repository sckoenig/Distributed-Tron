package vsp.trongame.middleware.serverstub;

import com.google.gson.Gson;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;
import vsp.trongame.middleware.util.ServiceCall;

import java.io.IOException;
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

    public Unmarshaller(ExecutorService executorService){
        this.gson = new Gson();
        this.executorService = executorService;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.remoteObjectRegister = new HashMap<>();

        try {
            new Receiver(this, executorService);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startServiceCallHandler(); //handles tasks in queue
    }

    private void startServiceCallHandler() {
        executorService.execute(() -> {
            try {
                byte[] message = messageQueue.take();
                String json = new String(message, StandardCharsets.UTF_8);
                ServiceCall call = gson.fromJson(json, ServiceCall.class);

                IRemoteObject remoteObject = remoteObjectRegister.get(call.serviceId());
                if (remoteObject != null)
                    remoteObject.call(call.serviceId(), call.intParameters(), call.stringParameters());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    public void addToQueue(byte[] message) {
        messageQueue.add(message);
    }

    @Override
    public void registerRemoteObject(int serviceID, IRemoteObject remoteObject) {
        remoteObjectRegister.put(serviceID, remoteObject);
    }
}

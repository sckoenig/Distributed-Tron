package vsp.trongame.middleware.serverstub;

import com.google.gson.Gson;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;
import vsp.trongame.middleware.clientstub.InvocationTask;
import vsp.trongame.middleware.util.ServiceCall;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Unmarshaller implements IUnmarshaller, IRegister {

    private final Map<Integer, IRemoteObject> register;

    private final BlockingQueue<byte[]> queue;



    public Unmarshaller(){
        queue = new LinkedBlockingQueue<>();
        register = new HashMap<>();

        new Thread(() ->{
            try {
                byte[] message = queue.take();
                String json = new String(message, StandardCharsets.UTF_8);
                Gson gson = new Gson();
                ServiceCall call = gson.fromJson(json, ServiceCall.class);

                IRemoteObject remoteObject = register.get(call.serviceId());
                if (remoteObject != null) remoteObject.call(call.serviceId(), call.intParameters(), call.stringParameters());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

    }
    @Override
    public void addToQueue(byte[] message) {
        queue.add(message);
    }

    @Override
    public void registerRemoteObject(int serviceID, IRemoteObject remoteObject) {
        register.put(serviceID, remoteObject);
    }
}

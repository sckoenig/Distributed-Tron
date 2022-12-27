package vsp.trongame.middleware.clientstub;

import com.google.gson.Gson;
import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.middleware.namingservice.INamingService;
import vsp.trongame.middleware.namingservice.NameResolver;
import vsp.trongame.middleware.util.ServiceCall;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Marshaller implements IRemoteInvocation {

    private final BlockingQueue<InvocationTask> queue;
    private final ISender sender;

    INamingService namingService;

    public Marshaller() throws SocketException {
        queue = new LinkedBlockingQueue<>();
        sender = new Sender();
        namingService = new NameResolver();

        startTaskHandler();
    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int[] intParameters, String... stringParameters) {
        ISender.Protocol protocol = type == InvocationType.RELIABLE? ISender.Protocol.TCP : ISender.Protocol.UDP;
        queue.add(new InvocationTask(new ServiceCall(serviceID, intParameters, stringParameters), remoteID, protocol));
    }

    private void startTaskHandler(){
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    InvocationTask task = queue.take();

                    Gson gson = new Gson();
                    byte[] message = gson.toJson(task.serviceCall()).getBytes(StandardCharsets.UTF_8);

                    InetSocketAddress address = namingService.lookupService(task.remoteId(), task.serviceCall().serviceId());

                    sender.send(message, address, task.protocol());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}

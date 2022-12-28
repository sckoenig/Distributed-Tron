package vsp.trongame.middleware.clientstub;

import com.google.gson.Gson;
import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.middleware.namingservice.INamingService;
import vsp.trongame.middleware.namingservice.NameResolver;
import vsp.trongame.middleware.util.InvocationTask;
import vsp.trongame.middleware.util.ServiceCall;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class Marshaller implements IRemoteInvocation {

    private final BlockingQueue<InvocationTask> queue;
    private final ISender sender;
    private final ExecutorService executorService;
    private final Gson gson;

    INamingService namingService;

    public Marshaller(ExecutorService executorService) throws SocketException {
        this.gson = new Gson();
        this.executorService = executorService;
        this.queue = new LinkedBlockingQueue<>();
        this.sender = new Sender();
        this.namingService = new NameResolver();

        startInvocationTaskHandler(); //handles tasks in queue
    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int[] intParameters, String... stringParameters) {
        ISender.Protocol protocol = type == InvocationType.RELIABLE? ISender.Protocol.TCP : ISender.Protocol.UDP;
        queue.add(new InvocationTask(new ServiceCall(serviceID, intParameters, stringParameters), remoteID, protocol));
    }

    private void startInvocationTaskHandler(){
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {

                    InvocationTask task = queue.take();
                    byte[] message = gson.toJson(task.serviceCall()).getBytes(StandardCharsets.UTF_8);

                    InetSocketAddress address = namingService.lookupService(task.remoteId(), task.serviceCall().serviceId());
                    sender.send(message, address, task.protocol());

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}

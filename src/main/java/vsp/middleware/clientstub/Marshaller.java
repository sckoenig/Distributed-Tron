package vsp.middleware.clientstub;

import com.google.gson.Gson;
import vsp.middleware.IRemoteInvocation;
import vsp.middleware.namingservice.INamingService;
import vsp.middleware.ServiceCall;

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
    private final INamingService namingService;

    public Marshaller(ExecutorService executorService, INamingService namingService) throws SocketException {
        this.gson = new Gson();
        this.executorService = executorService;
        this.queue = new LinkedBlockingQueue<>();
        this.sender = new Sender();
        this.namingService = namingService;

        runInvocationTaskHandler(); //handles tasks in queue
    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int[] intParameters, String... stringParameters) {
        ISender.Protocol protocol = type == InvocationType.RELIABLE? ISender.Protocol.TCP : ISender.Protocol.UDP;
        queue.add(new InvocationTask(new ServiceCall(serviceID, intParameters, stringParameters), remoteID, protocol));
    }

    private void runInvocationTaskHandler(){
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    InvocationTask task = queue.take();
                    System.out.println(gson.toJson(task.serviceCall()));
                    byte[] message  =  gson.toJson(task.serviceCall()).getBytes(StandardCharsets.UTF_8);

                    String result = namingService.lookupService(task.remoteId(), task.serviceCall().serviceId());
                    String[] split = result.split(":");
                    InetSocketAddress address = new InetSocketAddress(split[0], Integer.parseInt(split[1]));

                    sender.send(message, address, task.protocol());

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}

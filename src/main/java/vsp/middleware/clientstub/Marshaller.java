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

/**
 * Provides Remote Method Invocation.
 */
public class Marshaller implements IRemoteInvocation {

    private final BlockingQueue<InvocationTask> queue;
    private final ISender sender;
    private final ExecutorService executorService; //for task handler for queue
    private final Gson gson; //for marshalling
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

    /**
     * Orders a thread from executorService to handle {@link InvocationTask} objects in queue.
     */
    private void runInvocationTaskHandler(){
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()){
                try {
                    InvocationTask task = queue.take();
                    byte[] message = marshal(task.serviceCall());

                    InetSocketAddress address = lookUp(task);
                    if (address != null) sender.send(message, address, task.protocol());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    /**
     * Marshals the call to a service.
     * @param call the service call
     * @return service call as byte array
     */
    private byte[] marshal(ServiceCall call){
        return gson.toJson(call).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Looks up the address needed for a task.
     * @param task the task
     * @return the address
     */
    private InetSocketAddress lookUp(InvocationTask task){
        String result = namingService.lookupService(task.remoteId(), task.serviceCall().serviceId());
        InetSocketAddress address = null;

        if (result != null){
            String[] split = result.split(":");
            address = new InetSocketAddress(split[0], Integer.parseInt(split[1]));

        } else System.err.printf("Marshaller: Service could not be found: %s from %s%n", task.serviceCall().serviceId(), task.remoteId());

        return address;
    }
}

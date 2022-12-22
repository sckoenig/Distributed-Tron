package vsp.trongame.middleware.clientstub;

import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.middleware.ISender;
import vsp.trongame.middleware.InvocationTask;
import vsp.trongame.middleware.MethodCall;

import java.util.ArrayList;
import java.util.List;

public class Marshaller implements IRemoteInvocation {

    private List<InvocationTask> tasks;
    private ISender sender;

    public Marshaller() {
        tasks = new ArrayList<>();
        sender = new Sender();

    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int... parameters) {
        ISender.Protocol protocol = type == InvocationType.RELIABLE? ISender.Protocol.TCP : ISender.Protocol.UDP;
        tasks.add(new InvocationTask(new MethodCall(serviceID, parameters), remoteID, protocol));
    }
}

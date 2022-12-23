package vsp.trongame.middleware.clientstub;

import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.middleware.util.ServiceCall;

import java.util.ArrayList;
import java.util.List;

public class Marshaller implements IRemoteInvocation {

    private final List<InvocationTask> tasks;
    private final ISender sender;

    public Marshaller() {
        tasks = new ArrayList<>();
        sender = new Sender();

    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int[] intParameters, String... stringParameters) {
        ISender.Protocol protocol = type == InvocationType.RELIABLE? ISender.Protocol.TCP : ISender.Protocol.UDP;
        tasks.add(new InvocationTask(new ServiceCall(serviceID, intParameters, stringParameters), remoteID, protocol));
    }
}

package vsp.trongame.middleware.namingservice;

import java.net.InetAddress;

public interface INamingService {

    InetAddress lookupService(long calleeId, int serviceId);

    void registerService(String remoteID, int serviceID, InetAddress address);

    void unregisterService(String remoteID);

}

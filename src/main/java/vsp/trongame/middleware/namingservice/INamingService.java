package vsp.trongame.middleware.namingservice;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public interface INamingService {

    InetSocketAddress lookupService(String remoteID, int serviceId);

    void registerService(String remoteID, int serviceID, InetAddress address);

    void unregisterService(String remoteID);

}

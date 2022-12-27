package vsp.trongame.middleware.namingservice;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class NameResolver implements INamingService{
    @Override
    public InetSocketAddress lookupService(String remoteID, int serviceId) {
        return null;
    }

    @Override
    public void registerService(String remoteID, int serviceID, InetAddress address) {

    }

    @Override
    public void unregisterService(String remoteID) {

    }
}

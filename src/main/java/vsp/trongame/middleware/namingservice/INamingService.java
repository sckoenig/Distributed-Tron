package vsp.trongame.middleware.namingservice;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public interface INamingService {

    byte REGISTER = 1;
    byte UNREGISTER = 2;
    byte LOOKUP = 3;
    byte RESPONSE = 4;

    String lookupService(String remoteID, int serviceId);

    void registerService(String remoteID, int serviceID, String address);

    void unregisterService(String remoteID);

}

package vsp.middleware.namingservice;

public interface INamingService {

    int NO_SERVICE = -1;
    String NO_ADDRESS = "";
    String NO_REMOTE_ID = "";
    byte REGISTER = 1;
    byte UNREGISTER = 2;
    byte LOOKUP = 3;
    byte RESPONSE = 4;

    String lookupService(String remoteID, int serviceId);

    void registerService(String remoteID, int serviceID, String address);

    void unregisterService(String remoteID);

}
